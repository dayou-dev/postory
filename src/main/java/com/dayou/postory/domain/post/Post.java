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
	private User user; // 반환값중 작성자의 닉네임을 별도의 컬럼으로 저장할 수 있으나 닉네임에 변경 시 반영이 안되는 경우가 있거나 닉네임 변경을 위한 쿼리가 나가기에 연관매핑을 그대로 사용

	private String title;

	private String content;
	private int commentCount; // @OneToMany 단순 조회용으로 사용하는 @OneToMany 대신 count 속성을 넣어 N+1 문제를 해소하고자 함
	private int likesCount;// @OneToMany 단순 조회용으로 사용하는 @OneToMany 대신 count 속성을 넣어 N+1 문제를 해소하고자 함

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
