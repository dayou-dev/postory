package com.dayou.postory.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dayou.postory.api.dto.LoginRequest;
import com.dayou.postory.api.dto.SignUpRequest;
import com.dayou.postory.domain.user.User;
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
	public ResponseEntity<String> login(@RequestBody LoginRequest request) {
		userService.login(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
