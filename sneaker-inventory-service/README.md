# Sneaker Inventory Service

## Overview
The **Sneaker Inventory Service** is a Spring Boot application designed to handle **high-concurrency inventory management** for a limited sneaker drop.

It ensures **no overselling** by using **soft reservations**, **database-level locking**, and **transactional consistency**, even when thousands of users attempt to reserve stock simultaneously.

This service acts as the **single source of truth for inventory**.

---

## Tech Stack
- **Java 21**
- **Spring Boot 3.3.6
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **HikariCP (Connection Pooling)**
- **REST APIs**

---

## Core Concepts

### Soft Reservation
- Stock is **temporarily reserved** when a user initiates checkout.
- Reserved stock is not sold until confirmation.
- If payment fails or expires, the stock is released.

### Concurrency Handling
- Uses **pessimistic write locking** at the database level.
- Prevents race conditions and overselling.
- Ensures atomic stock updates.

---

## Database Configuration

```properties
spring.application.name=sneaker-inventory-service
server.port=8082

spring.datasource.url=jdbc:postgresql://localhost:5432/sneaker_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.datasource.hikari.maximum-pool-size=30
```

PostgreSQL is used intentionally instead of H2 to validate real-world locking and connection pooling behavior.

---

##  Inventory Table Structure

```sql
CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    total_stock INTEGER NOT NULL,
    available_stock INTEGER NOT NULL,
    reserved_stock INTEGER NOT NULL
);
```

### Stock Meaning
- **total_stock** → Total stock added initially
- **available_stock** → Stock available for reservation
- **reserved_stock** → Temporarily reserved stock

---

## Locking Strategy

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT sd FROM SneakerDrop sd where sd.id = :id")
SneakerDrop lockById(Long id);
```

This ensures that only **one transaction can modify stock at a time**.

---

## REST API Endpoints

### 1) Reserve Stock
Reserves stock when a user clicks checkout.

```http
POST /inventory/reserve?productId=1&qty=1
```

Behavior:
- Locks inventory row
- Decreases available stock
- Increases reserved stock
- Throws `SoldOutException` if stock is insufficient

---

### 2) Release Stock
Releases stock when reservation expires or is cancelled.

```http
POST /inventory/release?productId=1&qty=1
```

Behavior:
- Locks inventory row
- Moves stock from reserved back to available

---

### 3) Confirm Stock
Finalizes the sale after payment confirmation.

```http
POST /inventory/confirm?productId=1&qty=1
```

Behavior:
- Locks inventory row
- Reduces reserved stock permanently

---

### 4) Get Stock Details
Fetches current inventory status.

```http
GET /inventory/details?productId=1
```

Response:
```json
{
  "totalStock": 50,
  "availableStock": 30,
  "reservedStock": 20
}
```

---

## Error Handling

### Sold Out Scenario
```json
{
  "status": 409,
  "error": "SOLD_OUT",
  "message": "Product is sold out and not available"
}
```

Handled using `@RestControllerAdvice`.

---

## How to Run

### 1) Set environment variables
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=skanda21@
```

### 2) Ensure PostgreSQL is running
Database name: `sneaker_db`

### 3) Start the service
```bash
mvn spring-boot:run
```

Service runs at:
```
http://localhost:8082
```

---

## Design Assumptions
- One inventory record per product
- Quantity-based reservations supported
- Expiry handling is managed externally (Order service / scheduler)
- Inventory service does not handle payments

---

## Author
**Skanda Prasad**
