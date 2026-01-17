package com.company.sneakerdrop.orderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.company.sneakerdrop.orderservice.client.SneakerDropInventoryCall;
import com.company.sneakerdrop.orderservice.entity.SneakerDrop;
import com.company.sneakerdrop.orderservice.exception.BasicException;
import com.company.sneakerdrop.orderservice.repository.SneakerDropRepository;
import com.company.sneakerdrop.orderservice.service.SneakerDropService;

@SpringBootTest(classes = SneakerOrderServiceApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
final class SneakerOrderTests {

	@Autowired
	private MockMvc mockMvc;

	@SpyBean
	private SneakerDropService service;

	@MockBean
	private SneakerDropRepository repository;

	@MockBean
	private SneakerDropInventoryCall inventoryCall;

	private SneakerDrop order;

	@BeforeEach
	void setup() {

		order = new SneakerDrop();
		order.setId(1L);
		order.setUserId("user1");
		order.setProductId(10L);
		order.setQuantity(2);
		order.setStatus("RESERVED");
		order.setExpiresAt(LocalDateTime.now().plusMinutes(5));

		when(repository.lockById(1L)).thenReturn(order);
		when(repository.save(any())).thenReturn(order);
	}

	/**
	 * This test case is used to verify that when checkout is successful, an order
	 * is created with status RESERVED.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void checkout_success() {
		SneakerDrop result = service.checkout("user1", 10L, 2);
		assertEquals("RESERVED", result.getStatus());
	}

	/**
	 * This test case check when the inventory service reports a sold-out condition,
	 * the checkout process fails.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void checkout_inventorySoldOut() {
		doThrow(new BasicException("SOLD_OUT", "Sold out")).when(inventoryCall).reserve(10L, 2);

		assertThrows(BasicException.class, () -> service.checkout("user1", 10L, 2));
	}

	/**
	 * This test is used to verify a reserved order is successfully paid.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void pay_success() {
		service.pay(1L);
		assertEquals("PAID", order.getStatus());
	}

	/**
	 * This test case ensures that payment is rejected when the order is not in the
	 * RESERVED state.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void pay_invalid_state() {
		order.setStatus("PAID");

		BasicException ex = assertThrows(BasicException.class, () -> service.pay(1L));

		assertEquals("INVALID_STATE", ex.getErrorCode());
	}

	/**
	 * This test case verifies that payment fails when the order reservation has
	 * expired.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void pay_order_expired() {
		order.setExpiresAt(LocalDateTime.now().minusMinutes(1));

		BasicException ex = assertThrows(BasicException.class, () -> service.pay(1L));

		assertEquals("ORDER_EXPIRED", ex.getErrorCode());
		verify(inventoryCall).release(10L, 2);
	}

	/**
	 * This test case ensures that cancelling an order updates its status to
	 * CANCELLED.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void cancel_order() {

		when(repository.findById(order.getId())).thenReturn(Optional.of(order));

		service.cancel(order.getProductId(), order.getId(), order.getQuantity());

		verify(inventoryCall).release(order.getProductId(), order.getQuantity());
	}

	/**
	 * This test verifies that the checkout API returns a successful response when
	 * valid request parameters are provided.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void controller_checkout_success() throws Exception {
		mockMvc.perform(post("/orders/checkout").param("userId", "user1").param("productId", "10").param("qty", "2"))
				.andExpect(status().isOk());
	}

	/**
	 * This test verifies that when the inventory service reports a sold-out
	 * condition.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void controller_checkout_conflict() throws Exception {

		doThrow(new BasicException("SOLD_OUT", "Sold out")).when(inventoryCall).reserve(10L, 2);

		mockMvc.perform(post("/orders/checkout").param("userId", "user1").param("productId", "10").param("qty", "2"))
				.andExpect(status().isConflict());
	}

	/**
	 * This test verifies that the payment API successfully processes a valid order
	 * payment request.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void controller_pay_success() throws Exception {
		mockMvc.perform(post("/orders/pay").param("orderId", "1")).andExpect(status().isOk());
	}

	/**
	 * This test ensures that the payment API returns HTTP 410 (Gone) when the order
	 * reservation has expired.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void controller_pay_expired() throws Exception {

		order.setExpiresAt(LocalDateTime.now().minusMinutes(1));

		mockMvc.perform(post("/orders/pay").param("orderId", "1")).andExpect(status().isGone());
	}
}
