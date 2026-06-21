package com.dayou.postory.global.response;

import lombok.Getter;

@Getter
public class GlobalResponse<T> {
	private T data;
	private String message;

	public GlobalResponse(T data, String message) {
		this.data = data;
		this.message = message;
	}

	public GlobalResponse(String message) {
		this.message = message;
	}

	public static <T> GlobalResponse<T> success(T data) {
		return new GlobalResponse<>(data, "api success");
	}

	public static GlobalResponse<Void> success() {
		return new GlobalResponse<>("api success");
	}
}
