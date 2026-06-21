package com.dayou.postory.global.response;

import lombok.Getter;

@Getter
public class GlobalResponse<T> {
	private String status;
	private T data;
	private String message;

	public GlobalResponse(String status, T data, String message) {
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public GlobalResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	public static <T> GlobalResponse<T> success(String status, T data) {
		return new GlobalResponse<>(status, data, "api success");
	}

	public static GlobalResponse<Void> success(String status) {
		return new GlobalResponse<>(status, "api success");
	}
}
