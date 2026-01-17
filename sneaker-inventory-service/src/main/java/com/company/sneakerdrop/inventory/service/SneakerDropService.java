package com.company.sneakerdrop.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.company.sneakerdrop.inventory.entity.SneakerDrop;
import com.company.sneakerdrop.inventory.exception.SoldOutException;
import com.company.sneakerdrop.inventory.repository.SneakerDropRepository;

import jakarta.transaction.Transactional;

@Service
public class SneakerDropService implements ISneakerDropInterface {

	public final SneakerDropRepository repository;

	SneakerDropService(SneakerDropRepository repository) {
		this.repository = repository;
	}

	/**
	 * It accepts a product id and quantity from the request and locks the product
	 * row to prevent concurrent updates and checks if enough stock is available. If
	 * yes, it reduces available stock and increases reserved stock atomically
	 * throws a SoldOutException if stock gets over.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 quantity
	 */

	@Transactional
	public void reserve(Long productId, Integer qty) {

		SneakerDrop sneakerDrop = repository.lockById(productId);

		if (sneakerDrop.getAvailableStock() < qty) {
			throw new SoldOutException();
		}

		sneakerDrop.setAvailableStock(sneakerDrop.getAvailableStock() - qty);
		sneakerDrop.setReservedStock(sneakerDrop.getReservedStock() + qty);

	}

	/**
	 * It locks the product row to avoid concurrent conflicts and reduces reserved
	 * stock and adds it back to available stock. This is typically used when a
	 * reservation is cancelled or expires.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 quantity
	 */

	@Transactional
	public void release(Long productId, Integer qty) {
	    SneakerDrop sneakerDrop = repository.lockById(productId);
	    
	    if (sneakerDrop == null) return; // product not found, just return

	    int reserved = sneakerDrop.getReservedStock();
	    int releaseQty = Math.min(reserved, qty); // only release what is actually reserved

	    sneakerDrop.setReservedStock(reserved - releaseQty);
	    sneakerDrop.setAvailableStock(sneakerDrop.getAvailableStock() + releaseQty);
	}

	/**
	 * It is used to fetch stock details. Loads the product by ID from the database
	 * and throws an error if not found. The response includes total stock,
	 * available stock, and reserved stock.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param id
	 */

	public SneakerDrop getStock(Long id) {
		return repository.findById(id).orElseThrow();
	}

	/**
	 * It locks the product row to prevent concurrent changes and reduces the
	 * reserved stock by the specified quantity, effectively confirming the sale.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 * @param1 productId
	 * @param2 quantity
	 */

	@Transactional
	public void confirm(Long productId, Integer qty) {
		SneakerDrop sneakerDrop = repository.lockById(productId);

		sneakerDrop.setReservedStock(sneakerDrop.getReservedStock() - qty);

	}

	/**
	 * It uses repository to fetch all product names from DB.
	 * 
	 * @author Skanda Prasad
	 * @version 1.0
	 * @since 15/01/2026
	 */
	
	public List<SneakerDrop> getAllProducts() {
	    return repository.findAll();
	}


}
