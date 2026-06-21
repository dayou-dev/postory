package com.dayou.postory.global.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	POST_NOT_FOUND("게시글이 존재하지 않습니다."),
	USER_NOT_FOUND("존재하지 않는 사용자입니다.."),
	COMMENT_NOT_FOUND("댓글이 존재하지 않습니다."),
	EMAIL_NOT_FOUND("존재하지 않는 이메일입니다."),
	EMAIL_DUPLICATE("이미 사용중인 이메일입니다."),
	PASSWORD_MISS_MATCH("비밀번호를 확인해주세요."),;
	private final String message;
}
