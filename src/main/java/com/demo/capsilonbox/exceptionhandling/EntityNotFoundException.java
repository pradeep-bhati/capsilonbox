package com.demo.capsilonbox.exceptionhandling;

public class EntityNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8123545284861986101L;
	
	public EntityNotFoundException(String message)
	{
		super(message);
	}

	public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
