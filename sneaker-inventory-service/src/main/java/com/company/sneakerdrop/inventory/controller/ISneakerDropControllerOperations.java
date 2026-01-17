package com.company.sneakerdrop.inventory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.sneakerdrop.inventory.entity.SneakerDrop;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Manage Inventory Service", description = "Sneaker Inventory Service")
public interface ISneakerDropControllerOperations {

	@Operation(summary = " Reserve the Stock ", description = """
			<ul>
			<li> This API is used to reserve sneaker stock for a product.
			<ol>
			<li> It accepts a quantity (qty) from the request.
			<li> Inside a transaction, it locks the product row to prevent concurrent updates.
			<li> It checks if enough stock is available.
			<li> If yes, it reduces available stock and increases reserved stock atomically.
			<li> If stock is insufficient, it throws a SoldOutException.
			</li>
			<ol>
			</ul>
			""")
	@PostMapping(value = "/reserve", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public void reserve(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer qty);
	

	@Operation(summary = " Release the Stock ", description = """
			<ul>
			<li> This API is used to release previously reserved sneaker stock.
			<ol>
			<li> It takes a quantity from the request.
			<li> It locks the product row to avoid concurrent conflicts.
			<li> It reduces reserved stock and adds it back to available stock.
			<li> This is typically used when a reservation is cancelled or expires.
			</li>
			<ol>
			</ul>
			""")
	@PostMapping(value = "/release", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public void release(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer qty);
	

	@Operation(summary = " Get Stock Details ", description = """
			<ul>
			<li> This API is used to return the current stock details of a sneaker product.
			<ol>
			<li> It is used to fetch stock details.
			<li> Loads the product by ID from the database and throws an error if not found.
			<li> The response includes total stock, available stock, and reserved stock.
			</li>
			<ol>
			</ul>
			""")
	@GetMapping(value = "/details", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public Map<String, Object> getStock(@RequestParam Long productId);
	

	@Operation(summary = " Confirm the Stocks ", description = """
			<ul>
			<li> This API is used to finalize a reservation and mark stock as sold.
			<ol>
			<li> It takes a quantity from the request which is by default setted as one and calls the service.
			<li> The service locks the product row to prevent concurrent changes.
			<li> It reduces the reserved stock by the specified quantity, effectively confirming the sale.
			</li>
			<ol>
			</ul>
			""")
	@PostMapping(value = "/confirm", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public void confirm(@RequestParam Long productId, @RequestParam(defaultValue = "1") Integer qty);
	

	@Operation(summary = " Get All Product Names ", description = """
			<ul>
			<li> This API is used to fetch all products present in DB.
			</li>
			</ul>
			""")
	@GetMapping(value = "/all", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<SneakerDrop> getProducts();

}
