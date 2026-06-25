package com.max.team_project_manager.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.max.team_project_manager.exception.EmailAlreadyInUseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmailAlreadyInUseException.class)
	public ResponseEntity<ErrorResponse> handle(EmailAlreadyInUseException ex) {

		ErrorResponse response = new ErrorResponse();

		response.setMessage("Email already in use");
		response.setError("EMAIL_ALREADY_IN_USE");

		return ResponseEntity
			.status(HttpStatus.CONFLICT)
			.body(response);
	}
}
