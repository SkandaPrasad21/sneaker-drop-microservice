# Sneaker Order Service

## Overview
The **Sneaker Order Service** manages the complete **order lifecycle** for a high-demand sneaker drop scenario.  
It works in coordination with the **Inventory Service** to guarantee **no overselling** by using a **soft reservation pattern**.

This service handles:
- Checkout and order creation
- Payment confirmation
- Order cancellation
- Automatic expiration and cleanup of unpaid reservations
- Frontend integration for real-time user interaction

---

## Tech Stack
- **Java 21**
- **Spring Boot 3.3.6**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **OpenFeign (REST client)**
- **Spring Scheduler**
- **HTML, CSS, JavaScript (Frontend)**

---

## Responsibilities

### Order Service
- Creates orders after inventory is successfully reserved
- Maintains order state and expiry time
- Confirms or releases inventory via Inventory Service
- Automatically cleans up expired reservations

### Inventory Service (External Dependency)
- Maintains stock consistency
- Handles concurrency and locking
- Exposes reserve / release / confirm APIs

---

## Order Lifecycle (Soft Reservation Flow)

1. User clicks **Checkout**
2. Order Service calls Inventory **/reserve**
3. Order is created with status `RESERVED`
4. Reservation expires after **5 minutes**
5. User clicks **Pay**
   - Inventory **/confirm**
   - Order marked as `PAID`
6. If user does nothing
   - Scheduler releases stock
   - Order is deleted
7. User can also **Cancel** before expiry

---

## Configuration

```properties
spring.application.name=sneaker-order-service
server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5432/sneaker_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.datasource.hikari.maximum-pool-size=50
spring.task.scheduling.pool.size=5

sneaker.inventory.service.base-url=http://localhost:8082/inventory
```

---

## Orders Table Structure

```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255),
    status VARCHAR(50),
    product_id BIGINT,
    expires_at TIMESTAMP NOT NULL,
    quantity INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

---

## Concurrency Strategy

### Inventory Calls
- All stock modifications are delegated to Inventory Service
- Prevents race conditions and overselling

### Order Locking
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
SneakerDrop lockById(Long id);
```

Used to safely transition order states (PAY / EXPIRE).

---

## Reservation Expiry Scheduler

```java
@Scheduled(fixedDelay = 30000)
public void cleanupExpiredReservations()
```

- Runs every 30 seconds
- Finds expired `RESERVED` orders
- Releases stock via Inventory Service
- Deletes expired orders

---

## REST API Endpoints

### 1) Checkout
```http
POST /orders/checkout?userId=user1&productId=1&qty=1
```

Creates a reserved order and starts expiry countdown.

---

### 2) Pay
```http
POST /orders/pay?orderId=1
```

- Confirms inventory
- Marks order as `PAID`

---

### 3) Cancel
```http
POST /orders/cancel?productId=1&quantity=1
```

- Releases inventory
- Cancels the order

---

## Error Handling

### Common Errors
| Error Code | HTTP Status | Meaning |
|----------|-------------|--------|
| SOLD_OUT | 409 | Inventory unavailable |
| INVALID_STATE | 409 | Order not reservable |
| ORDER_EXPIRED | 410 | Reservation expired |

Handled using `@RestControllerAdvice`.

---

## Frontend (UI)

### Features
- Product listing
- Live available & reserved stock
- Quantity selection
- Checkout / Pay / Cancel
- Countdown timer for reservation expiry

### Pages
- `home.html` – Product selection
- `product.html` – Checkout & payment screen

### Frontend → Backend Integration
- Inventory Service: `http://localhost:8082`
- Order Service: `http://localhost:8081`

---

## How to Run

### 1) Set environment variables
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=skanda21@
```

### 2) Start PostgreSQL
Ensure database `sneaker_db` exists.

### 3) Start services
```bash
# Inventory Service
mvn spring-boot:run

# Order Service
mvn spring-boot:run
```

### 4) Open UI
Open `home.html` in a browser.

---

## Author
**Skanda Prasad**
