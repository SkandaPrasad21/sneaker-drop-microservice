package com.company.sneakerdrop.inventory.service;

import java.util.List;

import com.company.sneakerdrop.inventory.entity.SneakerDrop;

public interface ISneakerDropInterface {
	
	public void reserve(Long productId, Integer qty);
	
	public void release(Long productId, Integer qty);
	
	public SneakerDrop getStock(Long id);
	
	public void confirm(Long productId, Integer qty);
	
	public List<SneakerDrop> getAllProducts();
}
