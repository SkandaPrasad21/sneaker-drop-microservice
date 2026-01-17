package com.company.sneakerdrop.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "inventory-service", url = "${sneaker.inventory.service.base-url}")
public interface SneakerDropInventoryCall {
	
	@PostMapping("/reserve")
	void reserve(@RequestParam Long productId, @RequestParam Integer qty);

	@PostMapping("/release")
	void release(@RequestParam Long productId, @RequestParam Integer qty);

	@PostMapping("/confirm")
	void confirm(@RequestParam Long productId, @RequestParam Integer qty);
}
