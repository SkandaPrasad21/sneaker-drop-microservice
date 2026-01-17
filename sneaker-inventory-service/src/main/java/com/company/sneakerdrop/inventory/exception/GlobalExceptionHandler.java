package com.company.sneakerdrop.inventory.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SoldOutException.class)
	public ResponseEntity<Map<String, Object>> handleSoldOut(SoldOutException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("timestamp", LocalDateTime.now(), "status", 409,
				"error", "SOLD_OUT", "message", ex.getMessage()));
	}

}
