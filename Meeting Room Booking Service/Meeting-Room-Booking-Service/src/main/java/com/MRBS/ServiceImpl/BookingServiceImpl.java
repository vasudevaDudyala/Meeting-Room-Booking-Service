package com.MRBS.ServiceImpl;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.MRBS.DTO.BookingListResponse;
import com.MRBS.DTO.BookingRequest;
import com.MRBS.DTO.RoomUtilizationResponse;
import com.MRBS.ExceptionHandling.BadRequestException;
import com.MRBS.ExceptionHandling.NotFoundException;
import com.MRBS.model.Booking;
import com.MRBS.model.Idempotency;
import com.MRBS.model.Room;
import com.MRBS.repository.BookingRepository;
import com.MRBS.repository.IdempotencyRepository;
import com.MRBS.repository.RoomRepository;
import com.MRBS.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    RoomRepository roomRepo;

    @Autowired
    BookingRepository bookingRepo;
    
    @Autowired
    
    IdempotencyRepository idRepo;

    @Override
    public Booking createBooking(BookingRequest req, String key) {
        Idempotency existing = idRepo
                .findByIdempotencyKeyAndOrganizerEmail(key, req.getOrganizerEmail())
                .orElse(null);

        if (existing != null) {
            Booking existingBooking = bookingRepo.findById(existing.getBookingId())
                    .orElseThrow(() -> new NotFoundException("Booking not found"));

            return existingBooking;
        }
        roomRepo.findById(req.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        if (req.getStartTime() == null || req.getEndTime() == null) {
            throw new BadRequestException("startTime and endTime are required");
        }

        if (!req.getStartTime().isBefore(req.getEndTime())) {
            throw new BadRequestException("startTime must be before endTime");
        }
        long minutes = Duration.between(req.getStartTime(), req.getEndTime()).toMinutes();
        if (minutes < 15 || minutes > 240) {
            throw new BadRequestException("Duration must be between 15 mins and 4 hours");
        }
        DayOfWeek day = req.getStartTime().getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            throw new BadRequestException("Bookings allowed only Mon-Fri");
        }
        int startHour = req.getStartTime().getHour();
        int endHour = req.getEndTime().getHour();

        if (startHour < 8 || endHour > 20) {
            throw new BadRequestException("Allowed between 08:00–20:00");
        }
        List<Booking> bookings =
                bookingRepo.findByRoomIdAndStatus(req.getRoomId(), "confirmed");

        for (Booking b : bookings) {
            if (req.getStartTime().isBefore(b.getEndTime()) &&
                req.getEndTime().isAfter(b.getStartTime())) {

                throw new BadRequestException("Booking overlaps with existing booking");
            }
        }
        Booking booking = new Booking();
        booking.setRoomId(req.getRoomId());
        booking.setTitle(req.getTitle());
        booking.setOrganizerEmail(req.getOrganizerEmail());
        booking.setStartTime(req.getStartTime());
        booking.setEndTime(req.getEndTime());
        booking.setStatus("confirmed");

        Booking saved = bookingRepo.save(booking);
        
        Idempotency id = new Idempotency();
        id.setIdempotencyKey(key);
        id.setOrganizerEmail(req.getOrganizerEmail());
        id.setBookingId(saved.getId());

        try {
            idRepo.save(id);
        } catch (DataIntegrityViolationException ex) {
            Idempotency existingRecord = idRepo
                    .findByIdempotencyKeyAndOrganizerEmail(key, req.getOrganizerEmail())
                    .orElseThrow(() -> new BadRequestException("Idempotency conflict"));

            return bookingRepo.findById(existingRecord.getBookingId())
                    .orElseThrow(() -> new BadRequestException("Booking not found"));
        }

        return saved;
    }
    private BookingRequest convertModeltoDTO(Booking b) {
        BookingRequest br = new BookingRequest();
        br.setRoomId(b.getRoomId());
        br.setTitle(b.getTitle());
        br.setOrganizerEmail(b.getOrganizerEmail());
        br.setStartTime(b.getStartTime());
        br.setEndTime(b.getEndTime());
        return br;
    }

    @Override
    public BookingListResponse getBookings(Long roomId, LocalDateTime from,LocalDateTime to, Integer limit,Integer offset) {
                                         
        List<Booking> bookings;
        if (roomId != null) {
            bookings = bookingRepo.findByRoomId(roomId);
        } else {
            bookings = bookingRepo.findAll();
        }
        List<Booking> filtered = bookings.stream()
                .filter(b -> {
                    if (from != null && b.getEndTime().isBefore(from)) return false;
                    if (to != null && b.getStartTime().isAfter(to)) return false;
                    return true;
                })
                .collect(Collectors.toList());

        int total = filtered.size();

        if (limit == null || limit <= 0) limit = 10;
        if (offset == null || offset < 0) offset = 0;

        int start = Math.min(offset, total);
        int end = Math.min(start + limit, total);

        List<Booking> paginated = filtered.subList(start, end);

        List<BookingRequest> items = paginated.stream()
                .map(this::convertBookingModeltoDTO)
                .collect(Collectors.toList());

        BookingListResponse response = new BookingListResponse();
        response.setItems(items);
        response.setTotal(total);
        response.setLimit(limit);
        response.setOffset(offset);

        return response;
    }

    private BookingRequest convertBookingModeltoDTO(Booking b) {
        BookingRequest br = new BookingRequest();
        br.setRoomId(b.getRoomId());
        br.setTitle(b.getTitle());
        br.setOrganizerEmail(b.getOrganizerEmail());
        br.setStartTime(b.getStartTime());
        br.setEndTime(b.getEndTime());
        return br;
    }

	@Override
	public BookingRequest cancelBooking(Long bookingId) {
	    Booking booking = bookingRepo.findById(bookingId)
	            .orElseThrow(() -> new NotFoundException("Booking not found"));

	    if ("cancelled".equalsIgnoreCase(booking.getStatus())) {
	        return convertModeltoDTO(booking);
	    }

	    LocalDateTime now = LocalDateTime.now();

	    long minutes = Duration.between(now, booking.getStartTime()).toMinutes();
	    
	    if (minutes < 60) {
	        throw new BadRequestException("Cannot cancel within 1 hour of start time");
	    }
	    booking.setStatus("cancelled");

	    Booking updated = bookingRepo.save(booking);

	    return convertModeltoDTO(updated);
	}

	@Override
	public List<RoomUtilizationResponse> getUtilization(LocalDateTime from, LocalDateTime to) {
		if (from == null || to == null) {
	        throw new BadRequestException("from and to date are required");
	    }
	    if (from.isAfter(to)) {
	        throw new BadRequestException("from date must be before to date");
	    }
		List<Room> rooms = roomRepo.findAll();
	    List<RoomUtilizationResponse> result = new ArrayList<>();

	    for (Room room : rooms) {

	        List<Booking> bookings =
	                bookingRepo.findByRoomIdAndStatus(room.getId(), "confirmed");

	        double totalBookedHours = 0;

	        for (Booking b : bookings) {

	            if (b.getEndTime().isBefore(from) || b.getStartTime().isAfter(to)) {
	                continue;
	            }

	            LocalDateTime start = b.getStartTime().isBefore(from) ? from : b.getStartTime();
	            LocalDateTime end = b.getEndTime().isAfter(to) ? to : b.getEndTime();

	            long minutes = Duration.between(start, end).toMinutes();
	            totalBookedHours += minutes / 60.0;
	        }

	        double totalBusinessHours = calculateBusinessHours(from, to);

	        double utilization = totalBusinessHours == 0 ? 0 :
	                totalBookedHours / totalBusinessHours;

	        RoomUtilizationResponse res = new RoomUtilizationResponse();
	        res.setRoomId(room.getId());
	        res.setRoomName(room.getName());
	        res.setTotalBookingHours(totalBookedHours);
	        res.setUtilizationPercent(utilization);

	        result.add(res);
	    }

	    return result;
	}

	private double calculateBusinessHours(LocalDateTime from, LocalDateTime to) {
		 double totalHours = 0;
		    LocalDate current = from.toLocalDate();
		    LocalDate endDate = to.toLocalDate();
		    while (!current.isAfter(endDate)) {
		        DayOfWeek day = current.getDayOfWeek();
		        if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
		            totalHours += 12; 
		        }
		        current = current.plusDays(1);
		    }
		    return totalHours;
	}

	
}