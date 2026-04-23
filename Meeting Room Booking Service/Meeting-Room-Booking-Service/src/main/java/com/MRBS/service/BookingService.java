package com.MRBS.service;

import java.time.LocalDateTime;
import java.util.List;

import com.MRBS.DTO.BookingListResponse;
import com.MRBS.DTO.BookingRequest;
import com.MRBS.DTO.RoomUtilizationResponse;
import com.MRBS.model.Booking;

public interface BookingService {
     Booking createBooking(BookingRequest req, String idempotencyKey);
	 BookingListResponse getBookings(Long roomId, LocalDateTime from, LocalDateTime to, Integer limit, Integer offsetroomId);
	 BookingRequest cancelBooking(Long bookingId);
	 List<RoomUtilizationResponse> getUtilization(LocalDateTime from, LocalDateTime to);
}
