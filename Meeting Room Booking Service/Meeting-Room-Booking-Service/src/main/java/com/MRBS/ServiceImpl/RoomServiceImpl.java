package com.MRBS.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MRBS.DTO.RoomRequest;
import com.MRBS.ExceptionHandling.BadRequestException;

import com.MRBS.model.Room;
import com.MRBS.repository.RoomRepository;
import com.MRBS.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService{
	
	@Autowired
	RoomRepository repo;
	@Override
	public RoomRequest CreateRoom(RoomRequest req) {
		 Room room=convertDTOtoRoom(req);
		    if (room.getCapacity() < 1 ) {
			    throw new BadRequestException("Capacity must be >= 1");
			}

			if (room.getName() == null || room.getName().trim().isEmpty()) {
			    throw new BadRequestException("Room name is required");
			}

			repo.findByNameIgnoreCase(room.getName())
			    .ifPresent(r -> {
			        throw new BadRequestException("Room name already exists");
			    });

        return convertRoomToDTO(repo.save(room));
	}
	public Room convertDTOtoRoom(RoomRequest req) {
		Room r=new Room();
		r.setName(req.getName());
		r.setCapacity(req.getCapacity());
		r.setFloor(req.getFloor());
		r.setAmenities(req.getAmenities());
		return r;
	}
	public RoomRequest convertRoomToDTO(Room r) {
		RoomRequest req=new RoomRequest();
		req.setId(r.getId());
		req.setName(r.getName());
		req.setCapacity(r.getCapacity());
		req.setFloor(r.getFloor());
		req.setAmenities(r.getAmenities());
		return req;
	}
	@Override
	public List<Room> getRooms(Integer minCapacity, String amenity) {
		if (minCapacity != null && minCapacity < 1) {
	        throw new BadRequestException("minCapacity must be >= 1");
	    }
		if (minCapacity == null && amenity == null) {
	        throw new BadRequestException("mincapacity and amenity can't be null.");
	    }
        if (minCapacity != null && amenity != null) {
            return repo.findByCapacityGreaterThanEqualAndAmenitiesContaining(minCapacity, amenity);
        }
        if (minCapacity != null) {
            return repo.findByCapacityGreaterThanEqual(minCapacity);
        }
        if (amenity != null) {
            return repo.findByAmenitiesContaining(amenity);
        }
        
        return repo.findAll();
	}
	

}
