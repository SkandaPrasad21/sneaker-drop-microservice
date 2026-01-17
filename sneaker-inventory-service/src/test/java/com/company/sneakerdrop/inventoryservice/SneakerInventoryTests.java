package com.company.sneakerdrop.inventoryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.company.sneakerdrop.inventory.SneakerInventoryServiceApplication;
import com.company.sneakerdrop.inventory.entity.SneakerDrop;
import com.company.sneakerdrop.inventory.exception.SoldOutException;
import com.company.sneakerdrop.inventory.repository.SneakerDropRepository;
import com.company.sneakerdrop.inventory.service.SneakerDropService;

@SpringBootTest(classes = SneakerInventoryServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
final class SneakerInventoryTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SneakerDropRepository repository;

	@Autowired
	private SneakerDropService service;

	private SneakerDrop sneakerDrop;

	@BeforeEach
	void setup() {

		sneakerDrop = new SneakerDrop();
		sneakerDrop.setProductName("FULL_COVERAGE_SHOE");
		sneakerDrop.setTotalStock(10);
		sneakerDrop.setAvailableStock(10);
		sneakerDrop.setReservedStock(0);
		sneakerDrop = repository.save(sneakerDrop);
	}

	/**
	 * This test case check valid scenario for reserve method of service class.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testReserve_valid() {
		service.reserve(sneakerDrop.getId(), 5);
		SneakerDrop sd = repository.findById(sneakerDrop.getId()).get();
		assertEquals(5, sd.getAvailableStock());
		assertEquals(5, sd.getReservedStock());
	}

	/**
	 * This test case is used to reserve more than available stock which then throws
	 * SoldOutException.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testReserve_exceedsStock() {
		assertThrows(SoldOutException.class, () -> service.reserve(sneakerDrop.getId(), 20));
	}

	/**
	 * This test case check valid scenario for release method of service class.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testRelease() {
		service.reserve(sneakerDrop.getId(), 5);
		service.release(sneakerDrop.getId(), 3);
		SneakerDrop sd = repository.findById(sneakerDrop.getId()).get();
		assertEquals(8, sd.getAvailableStock());
		assertEquals(2, sd.getReservedStock());
	}

	/**
	 * This test case check valid scenario for confirm method of service class.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testConfirm() {
		service.reserve(sneakerDrop.getId(), 5);
		service.confirm(sneakerDrop.getId(), 3);
		SneakerDrop sd = repository.findById(sneakerDrop.getId()).get();
		assertEquals(2, sd.getReservedStock());
	}

	/**
	 * This test case check valid scenario for get stock method of service class.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testGetStock() {
		SneakerDrop sd = service.getStock(sneakerDrop.getId());
		assertEquals(sneakerDrop.getId(), sd.getId());
	}

	/**
	 * This test case check endpoint calls service and responds 200 OK.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testReserveEndpoint_valid() throws Exception {
		mockMvc.perform(post("/inventory/reserve").param("productId", sneakerDrop.getId().toString()).param("qty", "3"))
				.andExpect(status().isOk());
	}

	/**
	 * This test case check controller handles SoldOutException and returns HTTP 409
	 * CONFLICT.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testReserveEndpoint_exceedsStock() throws Exception {
		mockMvc.perform(
				post("/inventory/reserve").param("productId", sneakerDrop.getId().toString()).param("qty", "20"))
				.andExpect(status().isConflict());
	}

	/**
	 * This test case check controller calls correctly and returns 200 OK
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testReleaseEndpoint_valid() throws Exception {
		service.reserve(sneakerDrop.getId(), 5);
		mockMvc.perform(post("/inventory/release").param("productId", sneakerDrop.getId().toString()).param("qty", "3"))
				.andExpect(status().isOk());
	}

	/**
	 * This test case confirms controller-service integration for confirming
	 * reservations.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testConfirmEndpoint_valid() throws Exception {
		service.reserve(sneakerDrop.getId(), 4);
		mockMvc.perform(post("/inventory/confirm").param("productId", sneakerDrop.getId().toString()).param("qty", "2"))
				.andExpect(status().isOk());
	}

	/**
	 * This test case ensures controller returns stock info correctly.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */

	@Test
	void testGetStockEndpoint() throws Exception {
		mockMvc.perform(get("/inventory/details").param("productId", sneakerDrop.getId().toString()))
				.andExpect(status().isOk());
	}

}
