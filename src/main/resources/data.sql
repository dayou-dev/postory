-- ==============================================================
-- Postory 대용량 테스트 데이터 생성 스크립트 (MySQL 8.0+)
-- 실행: mysql -u dayou -p3874 postory < src/main/resources/data.sql
--
-- 생성량:
--   users   500명
--   post    100,000개
--   comment 각 post에 1~100개 (합계 약 505만 건)
--   likes   유저당 200~1,000개 (합계 약 15만 건)
--
-- 중간 임시 테이블 없이 재귀 CTE만으로 연관관계 매핑 후 직접 INSERT
-- IDENTITY 전략으로 순차 생성된 PK(users: 1~500, post: 1~100000)를
-- CTE에서 동일하게 계산하여 FK 정합성 보장
-- ==============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET SESSION cte_max_recursion_depth = 100000;

TRUNCATE TABLE likes;
TRUNCATE TABLE comment;
TRUNCATE TABLE post;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;
SET autocommit = 0;

-- ==============================================================
-- 1. 사용자 500명
--    created_at : 과거 1년 내 분산 (MOD(n*17, 365) 일 전)
--    updated_at : created_at 기준 최대 30일 이내 갱신
-- ==============================================================
INSERT INTO users (username, email, password, nickname, role, created_at, updated_at)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 500
)
SELECT
    CONCAT('user', n),
    CONCAT('user', n, '@example.com'),
    '$2a$10$dummyhash.placeholder.value0',
    CONCAT('닉네임', n),
    IF(n <= 5, 'ADMIN', 'NORMAL'),
    DATE_SUB(NOW(), INTERVAL MOD(n * 17, 365) DAY),
    DATE_SUB(NOW(), INTERVAL MOD(n * 7, 30) DAY)
FROM seq;

COMMIT;
SELECT CONCAT('[1/4] users 생성 완료: ', COUNT(*), '건') AS progress FROM users;

-- ==============================================================
-- 2. 게시글 100,000개
--    user_id  : 1~500 순환 배분 (IDENTITY PK 순서 보장)
--    created_at : 과거 6개월 내 분산 (MOD(n*13, 180) 일 전)
--    updated_at : created_at 이후 최대 30일 내 수정 반영
-- ==============================================================
INSERT INTO post (user_id, title, content, created_at, updated_at)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 100000
)
SELECT
    1 + MOD(n - 1, 500),
    CONCAT('게시글 제목 #', n),
    CONCAT('이것은 ', n, '번째 게시글의 내용입니다.'),
    DATE_SUB(NOW(), INTERVAL MOD(n * 13, 180) DAY),
    DATE_SUB(NOW(), INTERVAL MOD(n * 5, 30) DAY)
FROM seq;

COMMIT;
SELECT CONCAT('[2/4] post 생성 완료: ', COUNT(*), '건') AS progress FROM post;

-- ==============================================================
-- 3. 댓글: 각 post에 1~100개
--
--    post_ids CTE : 1~100,000 (INSERT로 생성된 PK와 동일)
--    slots    CTE : 1~100 (댓글 슬롯 번호)
--    두 CTE를 크로스 조인 후 WHERE로 게시글별 댓글 수 결정
--
--    댓글 수 : 1 + MOD(pid * 1553, 100)
--      - 1553(소수) → MOD 주기 100, 1~100 균등 분포
--    작성자  : 1 + MOD(pid * 31 + n * 17, 500)
--      - 31, 17 모두 500과 서로소 → 고른 유저 분포
-- ==============================================================
-- created_at : 과거 3개월 내 분산 (댓글은 게시글 이후 작성 가정)
-- updated_at : 댓글 수정은 드물므로 created_at 과 동일하게 설정
INSERT INTO comment (user_id, post_id, body, created_at, updated_at)
WITH RECURSIVE
    post_ids(pid) AS (
        SELECT 1
        UNION ALL
        SELECT pid + 1 FROM post_ids WHERE pid < 100000
    ),
    slots(n) AS (
        SELECT 1
        UNION ALL
        SELECT n + 1 FROM slots WHERE n < 100
    )
SELECT
    1 + MOD(pid * 31 + n * 17, 500),
    pid,
    CONCAT('댓글입니다. (', n, '번째)'),
    DATE_SUB(NOW(), INTERVAL MOD(pid * 11 + n * 3, 90) DAY),
    DATE_SUB(NOW(), INTERVAL MOD(pid * 11 + n * 3, 90) DAY)
FROM post_ids, slots
WHERE n <= 1 + MOD(pid * 1553, 100);

COMMIT;
SELECT CONCAT('[3/4] comment 생성 완료: ', COUNT(*), '건') AS progress FROM comment;

-- ==============================================================
-- 4. 좋아요: 유저당 200~1,000개
--
--    user_ids CTE : 1~500 (INSERT로 생성된 PK와 동일)
--    like_seq CTE : 1~1,000 (유저당 최대 좋아요 슬롯)
--    두 CTE를 크로스 조인 후 WHERE로 유저별 좋아요 수 결정
--
--    좋아요 수  : 200 + MOD(uid * 37, 801) → 200~1,000
--      - 37과 801은 서로소 → 균등 분포
--    대상 post  : 1 + MOD(uid * 97 + n * 1013, 100000)
--      - 97, 1013(소수) → 100,000개 post에 고른 분포
--    DISTINCT   : 해시 충돌로 인한 (user_id, post_id) 중복 제거
-- ==============================================================
-- created_at : 과거 6개월 내 분산
-- updated_at : 좋아요는 수정 불가이므로 created_at 과 동일
INSERT INTO likes (user_id, post_id, created_at, updated_at)
WITH RECURSIVE
    user_ids(uid) AS (
        SELECT 1
        UNION ALL
        SELECT uid + 1 FROM user_ids WHERE uid < 500
    ),
    like_seq(n) AS (
        SELECT 1
        UNION ALL
        SELECT n + 1 FROM like_seq WHERE n < 1000
    )
SELECT DISTINCT
    uid,
    1 + MOD(uid * 97 + n * 1013, 100000),
    DATE_SUB(NOW(), INTERVAL MOD(uid * 7 + n * 3, 180) DAY),
    DATE_SUB(NOW(), INTERVAL MOD(uid * 7 + n * 3, 180) DAY)
FROM user_ids, like_seq
WHERE n <= 200 + MOD(uid * 37, 801);

COMMIT;
SELECT CONCAT('[4/4] likes 생성 완료: ', COUNT(*), '건') AS progress FROM likes;

-- ==============================================================
-- 최종 요약
-- ==============================================================
SELECT '=== 데이터 생성 완료 ===' AS '';
SELECT 'users'    AS 테이블, COUNT(*) AS 건수 FROM users
UNION ALL
SELECT 'post',     COUNT(*) FROM post
UNION ALL
SELECT 'comment',  COUNT(*) FROM comment
UNION ALL
SELECT 'likes',    COUNT(*) FROM likes;
