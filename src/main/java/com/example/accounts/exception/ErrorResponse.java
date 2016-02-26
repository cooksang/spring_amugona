package com.example.accounts.exception;

import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private String message;
	
	private String code;
	
	private List<FieldError> errors;
	
	@SuppressWarnings("unused")
	public static class FieldError {
		private String field;
		private String value;
		private String reason;
	}
}
