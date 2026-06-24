package com.dayou.postory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayou.postory.api.dto.request.PostRequest;
import com.dayou.postory.api.dto.response.PostResponse;
import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.global.exception.ErrorCode;
import com.dayou.postory.global.exception.PostNotFoundException;
import com.dayou.postory.global.exception.UserNotFoundException;
import com.dayou.postory.repository.PostRepository;
import com.dayou.postory.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public PostResponse publishedPost(Long userId, PostRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = Post.builder().user(user).title(request.getTitle()).content(request.getContent()).build();
		postRepository.save(post);
		return new PostResponse(post.getId(), post.getUser().getNickname(), post.getTitle(), post.getContent());
	}

	public PostResponse getPostById(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() ->
			new PostNotFoundException(ErrorCode.POST_NOT_FOUND)
		);
		return new PostResponse(
			post.getId(),
			post.getUser().getUsername(),
			post.getTitle(),
			post.getContent()
		);
	}

	public List<PostResponse> getPosts() {
		List<Post> posts = postRepository.findAll();
		return posts.stream().map(post -> new PostResponse(
			post.getId(),
			post.getUser().getNickname(),
			post.getTitle(),
			post.getContent())
		).toList();
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
