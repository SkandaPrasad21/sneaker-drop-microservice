package com.company.sneakerdrop.orderservice.client;

import org.springframework.stereotype.Component;

import com.company.sneakerdrop.orderservice.exception.BasicException;

import feign.FeignException;

@Component
public class InventoryServiceAdapter {

	private final SneakerDropInventoryCall inventoryClient;

	public InventoryServiceAdapter(SneakerDropInventoryCall inventoryClient) {
		this.inventoryClient = inventoryClient;
	}

	public void reserve(Long productId, Integer qty) {

		try {
			inventoryClient.reserve(productId, qty);

		} catch (FeignException.Conflict ex) {
			throw new BasicException("SOLD_OUT", "Product is sold out or already reserved");
		}
	}

	public void release(Long productId, Integer quantity) {
		inventoryClient.release(productId, quantity);
	}

	public void confirm(Long productId, Integer qty) {
		inventoryClient.confirm(productId, qty);
	}

}
