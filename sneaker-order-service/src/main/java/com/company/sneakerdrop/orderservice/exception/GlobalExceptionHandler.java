package com.company.sneakerdrop.orderservice.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BasicException.class)
	public ResponseEntity<Map<String, Object>> handleInventorySoldOut(BasicException ex) {

		HttpStatus status = HttpStatus.CONFLICT;

		if ("ORDER_EXPIRED".equals(ex.getErrorCode())) {
			status = HttpStatus.GONE;
		}

		return ResponseEntity.status(status).body(Map.of("timestamp", LocalDateTime.now(), "status", status.value(),
				"error", ex.getErrorCode(), "message", ex.getMessage()));
	}

}
