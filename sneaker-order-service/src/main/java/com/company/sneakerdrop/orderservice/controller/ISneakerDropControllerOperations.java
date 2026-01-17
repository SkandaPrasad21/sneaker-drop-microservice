package com.company.sneakerdrop.orderservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.sneakerdrop.orderservice.entity.SneakerDrop;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Manage Order Service", description = "Sneaker Order Service")
public interface ISneakerDropControllerOperations {

	@Operation(summary = " Checkout ", description = """
			<ul>
			<li> This API is used to checkout the product.
			<ol>
			<li> It first reserves inventory to ensure stock availability and handle concurrency before creating an order.
			<li> If the reservation succeeds, a new SneakerDrop order is created with status RESERVED and an expiry time to support temporary holds.
			<li> Then order is then persisted in the database, ensuring consistency between inventory and orders.
			</li>
			<ol>
			</ul>
			""")
	@PostMapping(value = "/checkout", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public SneakerDrop checkout(@RequestParam String userId, @RequestParam Long productId, @RequestParam Integer qty);

	@Operation(summary = " Payment ", description = """
			<ul>
			<li> This API is used for payment.
			<ol>
			<li> It is used to finalize an order by taking the orderId.
			<li> The service locks the order record to prevent concurrent updates and ensure consistency during payment.
			<li> It validates the order state, allowing payment only if the order is in RESERVED status.
			<li> If the reservation has expired, the order is deleted and the reserved inventory is released back.
			<li> On successful validation, the inventory is confirmed and the order status is updated to PAID.
			</li>
			<ol>
			</ul>
			""")
	@PostMapping(value = "/pay", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public void pay(@RequestParam Long orderId);
	
	@Operation(summary = " Cancel ", description = """
			<ul>
			<li> This API is used for cancellation before expiry time gets over.
			<ol>
			<li> It will release the stock before expiry time.
			<li> It cancels the order and deletes from DB.
			</li>
			<ol>
			</ul>
			""")
	@PostMapping(value = "/cancel", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public void cancel(@RequestParam Long productId, @RequestParam Long orderId, @RequestParam Integer quantity);
}