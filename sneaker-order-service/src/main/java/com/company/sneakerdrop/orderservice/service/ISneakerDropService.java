package com.company.sneakerdrop.orderservice.service;

import com.company.sneakerdrop.orderservice.entity.SneakerDrop;

public interface ISneakerDropService {
	
	public SneakerDrop checkout(String userId, Long productId, Integer qty);
	
	public void pay(Long orderId);
	
	public void cancel(Long productId, Long orderId, Integer quantity);
	
}
