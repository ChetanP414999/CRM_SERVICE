package com.app.crm.exceptionHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.crm.dto.CustomerDto;
import com.app.crm.exceptions.CustomerObjectNotFoundExceptions;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHanlder {
	@ExceptionHandler(CustomerObjectNotFoundExceptions.class)
	public ResponseEntity<CustomerDto> onHandlingException(CustomerObjectNotFoundExceptions e,HttpServletRequest request){
		CustomerDto dto=new CustomerDto(e.getMessage(), LocalDateTime.now(),request.getRequestURI(), 
				HttpStatus.NOT_FOUND);
		return new ResponseEntity<CustomerDto>(dto,HttpStatus.NOT_FOUND);
	}
}
