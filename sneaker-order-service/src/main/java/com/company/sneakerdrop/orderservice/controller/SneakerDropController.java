package com.company.sneakerdrop.orderservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.sneakerdrop.orderservice.entity.SneakerDrop;
import com.company.sneakerdrop.orderservice.service.ISneakerDropService;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class SneakerDropController implements ISneakerDropControllerOperations{

	private final ISneakerDropService service;

	public SneakerDropController(ISneakerDropService service) {
		this.service = service;
	}

	/**
	 * This API is used to checkout the products.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 userId
	 * @param2 productId
	 * @param3 quantity
	 */
	
	@PostMapping("/checkout")
	public SneakerDrop checkout(@RequestParam String userId, @RequestParam Long productId, @RequestParam Integer qty) {
		return service.checkout(userId, productId, qty);
	}

	/**
	 * This API is used for payment.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param orderId
	 */
	
	@PostMapping("/pay")
	public void pay(@RequestParam Long orderId) {
		service.pay(orderId);
	}
	
	/**
	 * This API is used to cancel order before product gets expire.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 orderId
	 * @param3 quantity
	 */
	
	@PostMapping("/cancel")
	public void cancel(@RequestParam Long productId, @RequestParam Long orderId, @RequestParam Integer quantity) {
		service.cancel(productId, orderId, quantity);
	}

}
