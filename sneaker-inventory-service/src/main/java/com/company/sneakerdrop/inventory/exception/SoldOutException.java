package com.company.sneakerdrop.inventory.exception;

public class SoldOutException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Default message
    public SoldOutException() {
        super("Product is sold out and not available");
    }

}
