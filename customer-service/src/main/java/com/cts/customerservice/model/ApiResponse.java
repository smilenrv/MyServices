package com.cts.customerservice.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse <T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
	private transient T data;
	private boolean hasError;
	private String errorMessage;

}
