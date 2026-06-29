package com.dayou.postory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		List<Comment> comments = post.getComments();
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

	public List<PostResponse> getPosts() {
		List<Post> posts = postRepository.findAll();
		return posts.stream().map(post -> new PostResponse(
			post.getId(),
			post.getUser().getNickname(),
			post.getTitle(),
			post.getComments().size(),
			post.getLikes().size(),
			post.getCreatedAt(),
			post.getUpdatedAt()
		)).toList();
	}

	public Page<PostResponse> improvedGetPosts(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Post> posts = postRepository.findAll(pageable);
		return posts.map(post -> new PostResponse(post.getId(),
			post.getUser().getNickname(),
			post.getTitle(),
			post.getComments().size(),
			post.getLikes().size(),
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
