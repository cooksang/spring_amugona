package com.example.accounts;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.accounts.exception.ErrorResponse;

@ControllerAdvice
public class ExceptionAdvice {
	
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse validationException(ValidationException e) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode("validation");
		errorResponse.setMessage(e.getMessage());
		return errorResponse;
	}
}
