package com.cts.cardservice.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "apiResponse")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ApiResponse <T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
	private transient T data;
	private boolean hasError;
	private String errorMessage;

}
