package com.dayou.postory.domain.user;

import com.dayou.postory.domain.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String email;

	private String password;

	private String nickname;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder
	public User(String username, String email, String password, String nickname) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = Role.NORMAL;
	}
}
