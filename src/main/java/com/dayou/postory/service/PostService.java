package com.dayou.postory.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.dayou.postory.api.dto.request.PostRequest;
import com.dayou.postory.api.dto.response.CommentResponse;
import com.dayou.postory.api.dto.response.PostDetailResponse;
import com.dayou.postory.api.dto.response.PostResponse;
import com.dayou.postory.domain.comment.Comment;
import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.global.exception.ErrorCode;
import com.dayou.postory.global.exception.PostNotFoundException;
import com.dayou.postory.global.exception.UserNotFoundException;
import com.dayou.postory.repository.CommentRepository;
import com.dayou.postory.repository.PostRepository;
import com.dayou.postory.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public void publishedPost(Long userId, PostRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = Post.builder().user(user).title(request.getTitle()).content(request.getContent()).build();
		postRepository.save(post);
	}

	public PostDetailResponse getPostById(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() ->
			new PostNotFoundException(ErrorCode.POST_NOT_FOUND)
		);

		List<CommentResponse> commentResponses = new ArrayList<>();
		List<Comment> comments = commentRepository.findAllByPost(post);
		for (Comment comment : comments) {
			commentResponses.add(new CommentResponse(comment));
		}
		return new PostDetailResponse(
			post.getId(),
			post.getUser().getUsername(),
			post.getTitle(),
			post.getContent(),
			post.getCreatedAt(),
			post.getUpdatedAt(),
			commentResponses
		);
	}

	public Page<PostResponse> improvedGetPosts(int page, int size) {
		StopWatch stopWatch = new StopWatch("improvedGetPosts Performance");
		stopWatch.start("Fetch and Map Posts");

		Pageable pageable = PageRequest.of(page, size);
		Page<Post> posts = postRepository.findAll(pageable);
		Page<PostResponse> res = posts.map(post -> new PostResponse(post.getId(),
			post.getUser().getNickname(),
			post.getTitle(),
			post.getCommentCount(),
			post.getLikesCount(),
			post.getCreatedAt(),
			post.getUpdatedAt()));

		// 2. 측정 종료
		stopWatch.stop();
		// 3. 결과 출력 (콘솔 출력 또는 로거 사용)
		log.info("\n{}", stopWatch.prettyPrint()); // Slf4j 로거를 사용할 경우

		return res;
	}

	public Page<PostResponse> findWeeklyTopPosts(int page, int size) {
		LocalDateTime now = LocalDateTime.now().minusDays(7);
		Pageable pageable = PageRequest.of(page, size);
		Page<Post> posts = postRepository.findTop20ByCreatedAtAfterOrderByLikesCountDesc(now, pageable);
		return posts.map(post -> new PostResponse(post.getId(),
			post.getUser().getNickname(),
			post.getTitle(),
			post.getCommentCount(),
			post.getLikesCount(),
			post.getCreatedAt(),
			post.getUpdatedAt()));
	}

	@Transactional
	public void updatePost(Long userId, Long postId, PostRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = postRepository.findByUserAndId(user, postId);

		post.updatePost(request.getTitle(),
			request.getContent()
		);
	}

	@Transactional
	public void deletePost(Long userId, Long postId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = postRepository.findByUserAndId(user, postId);
		postRepository.delete(post);
	}

}
