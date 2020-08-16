package com.cts.cardservice.exception;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import com.cts.cardservice.config.CardPropConfig;
import com.cts.cardservice.model.ApiResponse;

@ControllerAdvice
@ResponseBody
public class CardExceptionHandler {

	private CardPropConfig prop;

	public CardExceptionHandler(CardPropConfig prop) {
		this.prop = prop;
	}

	@ExceptionHandler(CardServiceException.class)
	@ResponseBody
	public ResponseEntity<Object> handleException(CardServiceException ex) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(ApiResponse.builder().code(HttpStatus.EXPECTATION_FAILED.value()).hasError(true)
						.errorMessage(ex.getMessage()).message(HttpStatus.EXPECTATION_FAILED.name()).build());
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Object> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).hasError(true)
						.errorMessage(ex.getMessage()).message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build());
	}

	@ExceptionHandler(HttpClientErrorException.class)
	@ResponseBody
	public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponse.builder().code(HttpStatus.BAD_REQUEST.value()).hasError(true)
						.errorMessage("Error occurred while fetching Customer API : " + ex.getMessage())
						.message(HttpStatus.BAD_REQUEST.name()).build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		StringBuilder sb = new StringBuilder("");
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			sb.append(error.getField() + " : " + getMessage(error.getDefaultMessage()) + "; ");
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			sb.append(error.getObjectName() + " : " + getMessage(error.getDefaultMessage()) + "; ");
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ApiResponse.builder().code(HttpStatus.UNPROCESSABLE_ENTITY.value()).hasError(true)
						.errorMessage(sb.toString()).message(HttpStatus.UNPROCESSABLE_ENTITY.name()).build());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		if (ex.getConstraintViolations().isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder("");
		for (ConstraintViolation<?> c : ex.getConstraintViolations()) {
			sb.append(c.getPropertyPath() + " : " + getMessage(c.getMessage()) + "; ");

		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ApiResponse.builder().code(HttpStatus.UNPROCESSABLE_ENTITY.value()).hasError(true)
						.errorMessage(sb.toString()).message(HttpStatus.UNPROCESSABLE_ENTITY.name()).build());
	}

	private String getMessage(String defaultMsg) {
		String msg = prop.getErrMessage().get(defaultMsg);
		return msg != null ? msg : defaultMsg;
	}

}
