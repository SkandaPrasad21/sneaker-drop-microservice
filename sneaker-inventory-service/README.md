# sneaker-inventory-service
This is a Spring Boot–based microservice responsible for managing sneaker stock during high-demand drop events. It supports real-time stock visibility, safe stock reservation, and release handling with concurrency control. Handles concurrency and sold-out scenarios with proper error responses.

# Features
- View total and available sneaker stock
- Reserve stock safely during checkout (concurrency-safe)
- Release stock on order cancellation or expiry
- Prevent over-selling (Sold-Out handling)
- CORS enabled for frontend integration
- Designed for high-traffic flash sales

# Tech Stack
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Hibernate
- REST APIs
