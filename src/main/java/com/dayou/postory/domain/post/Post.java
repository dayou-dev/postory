package com.dayou.postory.domain.post;

import java.util.ArrayList;
import java.util.List;

import com.dayou.postory.domain.BaseTimeEntity;
import com.dayou.postory.domain.comment.Comment;
import com.dayou.postory.domain.like.Like;
import com.dayou.postory.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private String title;

	private String content;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post")
	private List<Like> likes = new ArrayList<>();

	@Builder
	public Post(User user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
	}

	public void updatePost(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
