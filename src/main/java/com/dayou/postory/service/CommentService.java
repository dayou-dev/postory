package com.dayou.postory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayou.postory.api.dto.request.CommentRequest;
import com.dayou.postory.domain.comment.Comment;
import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.global.exception.CommentNotFoundException;
import com.dayou.postory.global.exception.ErrorCode;
import com.dayou.postory.global.exception.PostNotFoundException;
import com.dayou.postory.global.exception.UserNotFoundException;
import com.dayou.postory.global.exception.UserUnauthorizedException;
import com.dayou.postory.repository.CommentRepository;
import com.dayou.postory.repository.PostRepository;
import com.dayou.postory.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createComment(Long userId, Long postId, CommentRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));

		Comment comment = Comment.builder().post(post).user(user).body(request.getBody()).build();
		commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(Long userId, Long postId, Long commentId, CommentRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));
		Comment comment = commentRepository.findByIdAndPost(commentId, post).orElseThrow(() -> new PostNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
		if (!comment.getUser().equals(user)) {
			throw new UserUnauthorizedException(ErrorCode.USER_UNAUTHORIZED);
		}
		comment.updateBody(request.getBody());

	}

	@Transactional
	public void deleteComment(Long userId, Long postId, Long commentId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));
		Comment comment = commentRepository.findByIdAndPost(commentId, post).orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
		if (!comment.getUser().equals(user)) {
			throw new UserUnauthorizedException(ErrorCode.USER_UNAUTHORIZED);
		}
		commentRepository.delete(comment);
	}
}
