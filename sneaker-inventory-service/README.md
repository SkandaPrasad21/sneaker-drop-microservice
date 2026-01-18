# sneaker-inventory-service
This is a Spring Boot–based microservice responsible for managing sneaker invrntory service. It supports real-time stock visibility, safe stock reservation, and release handling with concurrency control. Handles concurrency and sold-out scenarios with proper error responses.

# Features
- View reserved and available sneaker stock
- Reserve stock safely during checkout (concurrency-safe)
- Release stock on order cancellation or expiry
- Prevent over-selling (Sold-Out handling)

# Tech Stack
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Hibernate
- REST APIs
