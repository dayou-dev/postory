package com.dayou.postory.api;

import static com.dayou.postory.global.constant.SessionConst.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dayou.postory.api.dto.request.LoginRequest;
import com.dayou.postory.api.dto.request.SignUpRequest;
import com.dayou.postory.api.dto.response.UserResponse;
import com.dayou.postory.domain.user.User;
import com.dayou.postory.global.response.GlobalResponse;
import com.dayou.postory.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {
	private final UserService userService;
	private final HttpSession httpSession;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
		userService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/login")
	public ResponseEntity<GlobalResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request) {
		User user = userService.login(request);
		httpSession.setAttribute(LOGIN_USER, user.getId());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponse.success(new UserResponse(user.getNickname())));
	}
}
