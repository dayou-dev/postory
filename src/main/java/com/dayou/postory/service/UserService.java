package com.dayou.postory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayou.postory.api.dto.LoginRequest;
import com.dayou.postory.api.dto.SignUpRequest;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	public String signUp(SignUpRequest request) {
		User user = User.builder()
			.email(request.getEmail())
			.password(request.getPassword())
			.nickname(request.getNickname())
			.build();
		userRepository.save(user);
		return user.getNickname();
	}

	@Transactional
	public String login(LoginRequest request) {
		if (!userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("이메일을 확인해 주세요");
		}
		User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
			.orElseThrow(() -> new IllegalArgumentException("비밀번호를 확인해 주세요"));

		return user.getNickname();
	}
}
