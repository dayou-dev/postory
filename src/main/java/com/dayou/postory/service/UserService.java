package com.dayou.postory.service;

import static com.dayou.postory.global.constant.SessionConst.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dayou.postory.api.dto.request.LoginRequest;
import com.dayou.postory.api.dto.request.SignUpRequest;
import com.dayou.postory.api.dto.response.UserResponse;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.global.encrytion.SHA256EncryptionService;
import com.dayou.postory.global.response.exception.EmailDuplicateException;
import com.dayou.postory.global.response.exception.EmailNotFoundException;
import com.dayou.postory.global.response.exception.ErrorCode;
import com.dayou.postory.global.response.exception.MissMatchPasswordException;
import com.dayou.postory.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;
	private final SHA256EncryptionService encryptionService;
	private final HttpSession httpSession;

	@Transactional
	public UserResponse signUp(SignUpRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailDuplicateException(ErrorCode.EMAIL_DUPLICATE);
		}
		User user = User.builder()
			.email(request.getEmail())
			.username(request.getUsername())
			.password(encryptionService.encryptPassword(request.getPassword()))
			.nickname(request.getNickname())
			.build();
		userRepository.save(user);
		return new UserResponse(user.getNickname());
	}

	@Transactional
	public UserResponse login(LoginRequest request) {
		if (!userRepository.existsByEmail(request.getEmail())) {
			throw new EmailNotFoundException(ErrorCode.EMAIL_NOT_FOUND);
		}
		User user = userRepository.findByEmailAndPassword(request.getEmail(),
				encryptionService.encryptPassword(request.getPassword()))
			.orElseThrow(() -> new MissMatchPasswordException(ErrorCode.PASSWORD_MISS_MATCH));
		httpSession.setAttribute(LOGIN_USER, user.getId());
		return new UserResponse(user.getNickname());
	}
}
