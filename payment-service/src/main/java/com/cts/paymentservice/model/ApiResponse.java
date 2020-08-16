package com.cts.paymentservice.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
	private transient T data;
	private boolean hasError;
	private String errorMessage;

}
