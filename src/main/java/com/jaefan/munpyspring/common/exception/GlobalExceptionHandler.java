package com.jaefan.munpyspring.common.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.jaefan.munpyspring.user.presentation.exception.OAuthProcessingException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
		Map<String, String> errorMessages = new HashMap<>();

		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("max-file-size exceeded", "업로드 가능 용량은 최대 10MB 입니다.");
		return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateEntityException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateEntityException(DuplicateEntityException ex) {
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("error", ex.getMessage());
		return new ResponseEntity<>(errorMessages, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(OAuthProcessingException.class)
	public ResponseEntity<String> handleOAuthProcessingException(OAuthProcessingException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
