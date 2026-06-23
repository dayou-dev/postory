package com.dayou.postory.global.annotation;

import static com.dayou.postory.global.constant.SessionConst.*;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginUserArgumnetResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		/**
		 * 1. 사용자가 요청을 보냄
		 * 2. 필터 체인 통과후 DispatcherServlet 인스턴스로 진입
		 * 3. DispatcherServlet 인스턴스는 해당 요청을 수행할 HandlerMethod 인스턴스를 결정
		 * 4. DispatcherServlet 인스턴스는 RequestMappingHandlerAdapter 인스턴스에게 HandlerMethod 인스턴스로 전달
		 * 5. RequestMappingHandlerAdapter 인스턴스는 Handler 인스턴스에게 필요 인수 값을 요청
		 * 5-1. HandlerMethod 인스턴스는 자신에게 등록된 리졸버(resolver)들을 통해 필요한 메서드 인수 값을 셋팅
		 * 6. HandlerMethod 인스턴스는 자신과 연결된 컨트롤러의 메서드를 호출
		 */
		log.info("supports Parameter 실행");

		boolean hasAnnotation = parameter.hasParameterAnnotation(LoginUser.class); // 매개변수로 입력된 애노테이션이 일치한지 확인
		boolean parameterType = Long.class.isAssignableFrom(parameter.getParameterType()); // 애노테이션이 사용된 파라미터의 타입이 동일한지 확인
		return hasAnnotation && parameterType;
	}

	@Override
	public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		log.info("resolver argument 실행");
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

		return session.getAttribute(LOGIN_USER);
	}
}
