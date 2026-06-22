package com.dayou.postory.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	@NotBlank(message = "본문을 입력해주세요.")
	private String content;
}
