package com.dayou.postory.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
	private Long id;
	private String author;
	private String title;
	private String content;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private List<CommentResponse> comments;
}
