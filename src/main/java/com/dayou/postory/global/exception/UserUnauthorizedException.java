package com.dayou.postory.global.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserUnauthorizedException extends RuntimeException {
	private final ErrorCode errorCode;

	@Override
	public String getMessage() {
		return errorCode.getMessage();
	}
}
