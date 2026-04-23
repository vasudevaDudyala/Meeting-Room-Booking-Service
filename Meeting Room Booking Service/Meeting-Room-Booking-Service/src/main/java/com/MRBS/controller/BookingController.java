package com.MRBS.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MRBS.DTO.BookingListResponse;
import com.MRBS.DTO.BookingRequest;
import com.MRBS.DTO.RoomUtilizationResponse;
import com.MRBS.ExceptionHandling.BadRequestException;
import com.MRBS.ExceptionHandling.ConflictException;
import com.MRBS.ExceptionHandling.ErrorResponse;
import com.MRBS.model.Booking;
import com.MRBS.repository.BookingRepository;
import com.MRBS.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

   @Autowired
   BookingService bookingService;

   @PostMapping
   public ResponseEntity<String> create(@RequestBody BookingRequest req,@RequestHeader("Idempotency-Key") String key) {

       Booking booking = bookingService.createBooking(req, key);
       String message = "Booking created successfully with Id: "+ booking.getId() + " and status: " + booking.getStatus();

       return ResponseEntity.status(HttpStatus.CREATED) .body(message);
              
   }
    
    @GetMapping("getBookings")
    public BookingListResponse getBookings(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {

        return bookingService.getBookings(roomId, from, to, limit, offset);
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
    BookingRequest br= bookingService.cancelBooking(id);
        String message = "Booking canceled successfully ";
        return ResponseEntity.status(HttpStatus.CREATED) .body(message);
    }
    
    @GetMapping("/reports/room-utilization")
    public List<RoomUtilizationResponse> getUtilization(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {

        return bookingService.getUtilization(from, to);
    }
    
    
  
    
 //Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 400),
                HttpStatus.BAD_REQUEST
        );
    }

    // Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 409),
                HttpStatus.CONFLICT
        );
    }

    // Not Found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGeneric(RuntimeException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 404),
                HttpStatus.NOT_FOUND
        );
    }
}