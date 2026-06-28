package com.max.team_project_manager.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.max.team_project_manager.exception.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log =
		LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> handleApp(AppException ex) {
		log.warn("Handled {}: {}", ex.getCode(), ex.getMessage());

		return ResponseEntity
			.status(ex.getStatus())
			.body(new ErrorResponse(ex.getMessage(), ex.getCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
			.findFirst()
			.map(err -> err.getField() + ": " + err.getDefaultMessage())
			.orElse("Validation failed");

		log.warn("Validation failed: {}", message);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(message, "VALIDATION_ERROR"));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
		log.warn("Malformed request body: {}", ex.getMessage());

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse("Malformed request body", "MALFORMED_REQUEST"));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
		log.warn("Authentication failed: {}", ex.getMessage());

	return ResponseEntity
		.status(HttpStatus.UNAUTHORIZED)
		.body(new ErrorResponse("Invalid email or password", "AUTHENTICATION_FAILED"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
		log.error("Unhandled exception", ex);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse("An unexpected error occurred", "INTERNAL_ERROR"));
	}
}
