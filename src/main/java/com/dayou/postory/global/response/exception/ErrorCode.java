package com.dayou.postory.global.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	POST_NOT_FOUND("게시글이 존재하지 없습니다."),
	COMMENT_NOT_FOUND("댓글이 존재하지 않습니다.");
	private final String message;
}
