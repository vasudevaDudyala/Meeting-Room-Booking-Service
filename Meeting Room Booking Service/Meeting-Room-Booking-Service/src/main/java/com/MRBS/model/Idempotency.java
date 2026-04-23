package com.MRBS.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "idempotency",
    uniqueConstraints = @UniqueConstraint(columnNames = {"idempotencyKey", "organizerEmail"})
)
public class Idempotency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idempotencyKey;
    private String organizerEmail;
    private Long bookingId;
    public Long getId() { 
    	return id;
    }

    public String getIdempotencyKey() { 
    	return idempotencyKey; 
    }
    public void setIdempotencyKey(String idempotencyKey) {
    	this.idempotencyKey = idempotencyKey; 
    }

    public String getOrganizerEmail() { 
    	return organizerEmail; 
    }
    public void setOrganizerEmail(String organizerEmail) { 
    	this.organizerEmail = organizerEmail;
    }

    public Long getBookingId() { 
    	return bookingId; 
    }
    public void setBookingId(Long bookingId) {
    	this.bookingId = bookingId; 
    }
}