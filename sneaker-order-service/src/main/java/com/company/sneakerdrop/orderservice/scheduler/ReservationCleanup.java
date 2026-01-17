package com.company.sneakerdrop.orderservice.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.company.sneakerdrop.orderservice.client.InventoryServiceAdapter;
import com.company.sneakerdrop.orderservice.entity.SneakerDrop;
import com.company.sneakerdrop.orderservice.repository.SneakerDropRepository;

import jakarta.transaction.Transactional;

@Component
public class ReservationCleanup {

	private final SneakerDropRepository repository;
	private final InventoryServiceAdapter inventoryCall;

	public ReservationCleanup(SneakerDropRepository repository, InventoryServiceAdapter inventoryCall) {
		this.repository = repository;
		this.inventoryCall = inventoryCall;
	}

	@Scheduled(fixedDelay = 30000)
	@Transactional
	public void cleanupExpiredReservations() {

		List<SneakerDrop> expiredOrders = repository.findExpiredOrders("RESERVED", LocalDateTime.now());

		for (SneakerDrop order : expiredOrders) {

			inventoryCall.release(order.getProductId(), order.getQuantity());
			repository.delete(order);
		}
	}

}
