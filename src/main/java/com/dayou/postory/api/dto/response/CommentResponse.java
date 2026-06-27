package com.dayou.postory.api.dto.response;

import java.time.LocalDateTime;

import com.dayou.postory.domain.comment.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponse {
	private Long commentId;
	private String author;
	private String body;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

	@Builder
	public CommentResponse(Comment comment) {
		this.commentId = comment.getId();
		this.author = comment.getUser().getNickname();
		this.body = comment.getBody();
		this.createTime = comment.getCreatedAt();
		this.updateTime = comment.getUpdatedAt();
	}
}
