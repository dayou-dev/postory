package com.dayou.postory.global.annotation;

import static com.dayou.postory.global.constant.SessionConst.*;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.dayou.postory.global.exception.ErrorCode;
import com.dayou.postory.global.exception.UserUnAuthenticationException;
import com.dayou.postory.global.exception.UserUnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasAnnotation = parameter.hasParameterAnnotation(LoginUser.class); // 매개변수로 입력된 애노테이션이 일치한지 확인
		boolean parameterType = Long.class.isAssignableFrom(
			parameter.getParameterType()); // 애노테이션이 사용된 파라미터의 타입이 동일한지 확인
		return hasAnnotation && parameterType;
	}

	@Override
	public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute(LOGIN_USER) == null) {
			throw new UserUnAuthenticationException(ErrorCode.USER_UNAUTHENTICATE);
		}
		return session.getAttribute(LOGIN_USER);
	}
}
