package com.MRBS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MRBS.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomIdAndStatus(Long roomId, String status);
    List<Booking> findByRoomId(Long roomId);

}