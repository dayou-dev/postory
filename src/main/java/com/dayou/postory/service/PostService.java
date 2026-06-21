package com.dayou.postory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayou.postory.api.dto.request.PostCreateRequest;
import com.dayou.postory.domain.post.Post;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.global.response.exception.ErrorCode;
import com.dayou.postory.global.response.exception.UserNotFoundException;
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
	public void publishedPost(Long userId, PostCreateRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		Post post = Post.builder().user(user).title(request.getTitle()).content(request.getContent()).build();
		postRepository.save(post);
	}

}
