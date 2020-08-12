package com.cts.customerservice.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cts.customerservice.config.CustomerConfig;
import com.cts.customerservice.model.ApiResponse;

@ControllerAdvice
public class CustomerExceptionHandler {

	@Autowired
	private CustomerConfig prop;

	@ResponseBody
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<?> handleException(CustomerNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(ApiResponse.builder().code(HttpStatus.EXPECTATION_FAILED.value()).hasError(true)
						.errorMessage(ex.getMessage()).message(HttpStatus.EXPECTATION_FAILED.name()).build());
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).hasError(true)
						.errorMessage(ex.getMessage()).message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build());
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
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

	private String getMessage(String defaultMsg) {
		String msg = prop.getErrMessage().get(defaultMsg);
		return msg != null ? msg : defaultMsg;
	}

}
