package com.dayou.postory.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	NORMAL("일반"), ADMIN("관리자");

	private final String value;

}
