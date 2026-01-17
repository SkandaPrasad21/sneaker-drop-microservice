package com.company.sneakerdrop.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.sneakerdrop.inventory.entity.SneakerDrop;

import jakarta.persistence.LockModeType;

public interface SneakerDropRepository extends JpaRepository<SneakerDrop, Long> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT sd FROM SneakerDrop sd where sd.id = :id")
	SneakerDrop lockById(@Param("id") Long id);

}
