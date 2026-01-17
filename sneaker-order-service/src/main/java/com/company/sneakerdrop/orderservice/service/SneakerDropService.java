package com.company.sneakerdrop.orderservice.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.company.sneakerdrop.orderservice.client.SneakerDropInventoryCall;
import com.company.sneakerdrop.orderservice.entity.SneakerDrop;
import com.company.sneakerdrop.orderservice.exception.BasicException;
import com.company.sneakerdrop.orderservice.repository.SneakerDropRepository;

import jakarta.transaction.Transactional;

@Service
public class SneakerDropService implements ISneakerDropService {

	private final SneakerDropRepository repository;
	private final SneakerDropInventoryCall inventoryClient;

	public SneakerDropService(SneakerDropRepository repository, SneakerDropInventoryCall inventoryClient) {
		this.repository = repository;
		this.inventoryClient = inventoryClient;
	}

	/**
	 * It first reserves inventory to ensure stock availability and if the
	 * reservation succeeds, a new SneakerDrop order is created with status RESERVED
	 * and an expiry time to support temporary holds. Then order is then persisted
	 * in the database, ensuring consistency between inventory and orders.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 userId
	 * @param2 productId
	 * @param3 quantity
	 */

	@Transactional
	public SneakerDrop checkout(String userId, Long productId, Integer qty) {

		inventoryClient.reserve(productId, qty);

		SneakerDrop sneakerOrder = new SneakerDrop();
		sneakerOrder.setUserId(userId);
		sneakerOrder.setQuantity(qty);
		sneakerOrder.setProductId(productId);
		sneakerOrder.setStatus("RESERVED");
		sneakerOrder.setExpiresAt(LocalDateTime.now().plusMinutes(5));

		return repository.save(sneakerOrder);

	}

	/**
	 * It is used to finalize an order by taking the orderId by allowing payment
	 * only if the order is in RESERVED status. If the reservation has expired, the
	 * order is deleted and the reserved inventory is released back. On successful
	 * validation, the inventory is confirmed and the order status is updated to
	 * PAID.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param orderId
	 */

	@Transactional
	public void pay(Long orderId) {

		SneakerDrop sneakerOrder = repository.lockById(orderId);

		if (!"RESERVED".equals(sneakerOrder.getStatus())) {
			throw new BasicException("INVALID_STATE",
					"Order cannot be placed when state is : " + sneakerOrder.getStatus());
		}

		if (sneakerOrder.getExpiresAt().isBefore(LocalDateTime.now())) {
			repository.delete(sneakerOrder);
			inventoryClient.release(sneakerOrder.getProductId(), sneakerOrder.getQuantity());
			throw new BasicException("ORDER_EXPIRED", "Order reservation expired");
		}

		inventoryClient.confirm(sneakerOrder.getProductId(), sneakerOrder.getQuantity());

		sneakerOrder.setStatus("PAID");

	}

	/**
	 * It will release the stock before expiry time and cancels the order and
	 * deletes record from DB.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 orderId
	 * @param3 qty
	 */

	@Transactional
	public void cancel(Long productId, Long orderId, Integer qty) {

		Optional<SneakerDrop> orderDrop = repository.findById(orderId);
		SneakerDrop order = orderDrop.get();
		try {
			inventoryClient.release(productId, qty);
		} catch (Exception e) {
			System.err.println("Inventory release failed: " + e.getMessage());
		}

		repository.delete(order);
	}

}
