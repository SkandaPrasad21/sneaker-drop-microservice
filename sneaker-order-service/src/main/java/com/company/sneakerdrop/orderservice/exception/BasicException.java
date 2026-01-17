package com.company.sneakerdrop.orderservice.exception;

public class BasicException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private final String errorCode;

    public BasicException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
