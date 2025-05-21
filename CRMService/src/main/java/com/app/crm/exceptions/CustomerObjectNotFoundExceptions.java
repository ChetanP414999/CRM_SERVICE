package com.app.crm.exceptions;


@SuppressWarnings("serial")
public class CustomerObjectNotFoundExceptions  extends RuntimeException{

	public CustomerObjectNotFoundExceptions(String message) {
		super(message);
	}
}
