package com.company.sneakerdrop.orderservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.sneakerdrop.orderservice.entity.SneakerDrop;

import jakarta.persistence.LockModeType;

public interface SneakerDropRepository extends JpaRepository<SneakerDrop, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT sd FROM SneakerDrop sd WHERE sd.id = :id")
	SneakerDrop lockById(@Param("id") Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT sd FROM SneakerDrop sd WHERE sd.status = :status AND sd.expiresAt < :now")
	List<SneakerDrop> findExpiredOrders(@Param("status") String status, @Param("now") LocalDateTime now);
	
	SneakerDrop findByProductId(Long productId);
}
