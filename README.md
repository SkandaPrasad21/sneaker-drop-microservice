# Create the combined README.md file for download

## Overview
The Sneaker Drop – Order & Inventory System

A high-concurrency Sneaker Drop System built using Spring Boot 3.3.6 and PostgreSQL, designed to safely handle 10,000+ concurrent users competing for only 50 items — with zero overselling.

---

## Key Highlights

- Soft reservation with 5-minute expiry
- Automatic release of expired reservations
- Pessimistic locking & transactional safety
- Real PostgreSQL (no H2)
- HikariCP connection pooling
- Feign-based inter-service communication
- Simple HTML / CSS / JavaScript frontend
- Real-time inventory visibility

---

## System Architecture

Browser (HTML / JS)  
→ Order Service (8081)  
→ Inventory Service (8082)  
→ PostgreSQL

---

## Services Overview

### Inventory Service
Responsible for stock consistency and concurrency safety.

APIs:
- POST /inventory/reserve
- POST /inventory/release
- POST /inventory/confirm
- GET  /inventory/details
- GET  /inventory/all

Concurrency Control:
- Pessimistic row-level locking
- @Transactional boundaries
- Prevents overselling under heavy load

---

### Order Service
Handles checkout, payment, cancellation, and expiry lifecycle.

APIs:
- POST /orders/checkout
- POST /orders/pay
- POST /orders/cancel

Responsibilities:
- Creates soft reservations
- Manages 5-minute expiry window
- Coordinates with Inventory service
- Ensures transactional consistency

---

## Reservation Lifecycle

1. User clicks Checkout
2. Inventory is reserved
3. Order created with status RESERVED
4. User has 5 minutes to pay
5. On payment → inventory confirmed → order PAID
6. On expiry/cancel → inventory released automatically

---

## Expired Reservation Cleanup

- Scheduler runs every 30 seconds
- Finds expired RESERVED orders
- Releases inventory
- Deletes expired orders

---

## Database Tables

Inventory:
- id
- product_name
- total_stock
- available_stock
- reserved_stock

Orders:
- id
- user_id
- product_id
- quantity
- status
- expires_at
- created_at
- updated_at

---

##  Frontend

- home.html: Product selection
- product.html: Checkout & payment
- Real-time stock polling
- Reservation countdown timer

---

## How to Run

1. Start PostgreSQL and create database sneaker_db
2. Start Inventory Service (8082)
3. Start Order Service (8081)
4. Open home.html in browser
5. Simulate concurrent users

---

## Author

Skanda Prasad
"""
