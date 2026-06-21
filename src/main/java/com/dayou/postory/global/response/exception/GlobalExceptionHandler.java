package com.dayou.postory.global.response.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dayou.postory.global.response.ExceptionResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PostNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handlePostNotFoundException(HttpServletRequest request,
		PostNotFoundException ex) {
		log.error("PostNotFoundException", ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ExceptionResponse.exception(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(),
				LocalDateTime.now()));
	}

	@ExceptionHandler(EmailNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleEmailNotFoundException(HttpServletRequest request,
		PostNotFoundException ex) {
		log.error("EmailNotFoundException", ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ExceptionResponse.exception(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(),
				LocalDateTime.now()));
	}

	@ExceptionHandler(EmailDuplicateException.class)
	public final ResponseEntity<ExceptionResponse> handleEmailDuplicateException(HttpServletRequest request,
		PostNotFoundException ex) {
		log.error("EmailDuplicateException", ex);
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(ExceptionResponse.exception(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(),
				LocalDateTime.now()));
	}

	@ExceptionHandler(MissMatchPasswordException.class)
	public final ResponseEntity<ExceptionResponse> handleMissMatchPasswordException(HttpServletRequest request,
		PostNotFoundException ex) {
		log.error("MissMatchPasswordException", ex);
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(ExceptionResponse.exception(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(),
				LocalDateTime.now()));
	}
}
