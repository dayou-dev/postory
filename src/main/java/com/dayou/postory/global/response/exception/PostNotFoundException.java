package com.dayou.postory.global.response.exception;

public class PostNotFoundException extends RuntimeException {
	private ErrorCode errorCode;

	@Override
	public String getMessage() {
		return errorCode.getMessage();
	}
}
