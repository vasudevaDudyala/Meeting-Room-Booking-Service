package com.MRBS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MRBS.DTO.RoomRequest;
import com.MRBS.ExceptionHandling.BadRequestException;
import com.MRBS.ExceptionHandling.ConflictException;
import com.MRBS.ExceptionHandling.ErrorResponse;
import com.MRBS.model.Room;
import com.MRBS.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody RoomRequest req) {

        RoomRequest request= roomService.CreateRoom(req);
        String message = "Room created successfully with Id: " + request.getId();
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    
    @GetMapping("getRooms")
    public List<Room> getRooms(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) String amenity) {

        return roomService.getRooms(minCapacity, amenity);
    }
    
    
    
//    //  400 - Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 400),
                HttpStatus.BAD_REQUEST
        );
    }

//  409 - Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 409),
                HttpStatus.CONFLICT
        );
    }

//  404 - Not Found
    @ExceptionHandler(RuntimeException.class)
   public ResponseEntity<ErrorResponse> handleGeneric(RuntimeException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), 404),
                HttpStatus.NOT_FOUND
        );
    }
}