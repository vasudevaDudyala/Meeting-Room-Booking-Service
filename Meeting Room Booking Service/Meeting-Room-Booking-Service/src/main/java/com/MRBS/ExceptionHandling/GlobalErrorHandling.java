//package com.MRBS.ExceptionHandling;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//@RestController
//public class GlobalErrorHandling {
//
//    //  400 - Bad Request
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
//        return new ResponseEntity<>(
//                new ErrorResponse(ex.getMessage(), 400),
//                HttpStatus.BAD_REQUEST
//        );
//    }
//
////  409 - Conflict
//    @ExceptionHandler(ConflictException.class)
//    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
//        return new ResponseEntity<>(
//                new ErrorResponse(ex.getMessage(), 409),
//                HttpStatus.CONFLICT
//        );
//    }
//
////  404 - Not Found
//    @ExceptionHandler(RuntimeException.class)
//   public ResponseEntity<ErrorResponse> handleGeneric(RuntimeException ex) {
//        return new ResponseEntity<>(
//                new ErrorResponse(ex.getMessage(), 404),
//                HttpStatus.NOT_FOUND
//        );
//    }
//}