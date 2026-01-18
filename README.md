This is a Spring Boot–based microservice responsible for managing sneaker stock during high-demand drop events. It supports real-time stock visibility, safe stock reservation, and release handling with concurrency control. Handles concurrency and sold-out scenarios with proper error responses. This service handles both inventory and order where all product details are stored inside inventory service and all order related data like order id, status, ordered qty gets stored in order. From frontend order service will be called and via order inventory service gets invoked. 

Features:
Product Names will be displayed so user can choose according to their wish.
Each products will display their details like resrved snesker stock and available sneaker stock.
Checkout, Pay and Cancel are three main functionalities used in this service.
Checkout will Reserve stock safely(concurrency-safe)
Pay will confirm the stock and store in DB.
Cancel will Release stock on order and also stock can also be released after time expires.
Prevents over-selling (Sold-Out handling)
CORS enabled for frontend integration
Designed for high-traffic flash sales

Tech Stack
Java 21
Spring Boot
Spring Web
Spring Data JPA
Feign Client
Postgre SQL
Hibernate
REST APIs
