package com.dayou.postory.global.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailNotFoundException extends RuntimeException {
	private final ErrorCode errorCode;

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
