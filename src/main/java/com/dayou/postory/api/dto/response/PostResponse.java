package com.dayou.postory.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
	private Long id;
	private String author;
	private String title;
	private String content;
}
