# Meeting-Room-Booking-Service

The Meeting Room Booking Service is a Spring Boot–based RESTful application designed to manage meeting room reservations efficiently. It allows users to create rooms, book available slots, cancel bookings, and view room utilization reports.

The system enforces key business rules such as preventing overlapping bookings, restricting booking duration and working hours, and allowing cancellations only within defined limits. It also implements idempotent booking creation to ensure that repeated requests do not result in duplicate bookings, making the system reliable in real-world scenarios.

The application follows a layered architecture with clear separation between controller, service, and persistence layers, ensuring maintainability and scalability. Additionally, it provides filtering capabilities for rooms and supports detailed reporting on room utilization based on booking data.
