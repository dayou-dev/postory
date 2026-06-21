package com.dayou.postory.global.response.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailNotFoundException extends RuntimeException {
	private final ErrorCode errorCode;

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
