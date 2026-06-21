package com.dayou.postory.global.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
	private HttpStatus status;
	private String message;
	private String path;
	private LocalDateTime timestamp;

	public static ExceptionResponse exception(HttpStatus status, String message, String path, LocalDateTime timestamp) {
		return new ExceptionResponse(status, message, path, timestamp);
	}
}
