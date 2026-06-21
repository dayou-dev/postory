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
import com.dayou.postory.global.response.GlobalResponse;
import com.dayou.postory.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
		userService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/login")
	public ResponseEntity<GlobalResponse<UserResponse>> login(@RequestBody LoginRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponse.success(userService.login(request)));
	}
}
