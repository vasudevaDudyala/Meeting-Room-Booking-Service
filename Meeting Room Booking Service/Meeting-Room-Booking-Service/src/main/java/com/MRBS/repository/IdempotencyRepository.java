package com.MRBS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MRBS.model.Idempotency;

public interface IdempotencyRepository extends JpaRepository<Idempotency, Long> {
    Optional<Idempotency> findByIdempotencyKeyAndOrganizerEmail(String key, String email);
}