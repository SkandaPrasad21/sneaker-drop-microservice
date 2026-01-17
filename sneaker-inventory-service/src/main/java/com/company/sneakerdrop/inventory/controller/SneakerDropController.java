package com.company.sneakerdrop.inventory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.sneakerdrop.inventory.entity.SneakerDrop;
import com.company.sneakerdrop.inventory.service.ISneakerDropInterface;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*")
public class SneakerDropController implements ISneakerDropControllerOperations {

	public final ISneakerDropInterface sneakerDropService;

	public SneakerDropController(ISneakerDropInterface sneakerDropService) {
		this.sneakerDropService = sneakerDropService;
	}

	/**
	 * This API is used to reserve sneaker stock for a product.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 quantity
	 */
	@PostMapping("/reserve")
	public void reserve(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer qty) {
		sneakerDropService.reserve(productId, qty);
	}

	/**
	 * This API is used to release previously reserved sneaker stock.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 quantity
	 */
	@PostMapping("/release")
	public void release(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer qty) {
		sneakerDropService.release(productId, qty);
	}

	/**
	 * This API is used to return the current stock details of a sneaker product.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param productId
	 */
	@GetMapping("/details")
	public Map<String, Object> getStock(@RequestParam Long productId) {
		SneakerDrop sd = sneakerDropService.getStock(productId);

		return Map.of("totalStock", sd.getTotalStock(), "availableStock", sd.getAvailableStock(), "reservedStock",
				sd.getReservedStock());
	}

	/**
	 * This API is used to finalize a reservation and mark stock as sold.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 quantity
	 */
	@PostMapping("/confirm")
	public void confirm(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer qty) {

		sneakerDropService.confirm(productId, qty);
	}
	
	/**
	 * This API is used to get all products list from DB.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */
	
	@GetMapping("/all")
	public List<SneakerDrop> getProducts() {
	    return sneakerDropService.getAllProducts();
	}


}
