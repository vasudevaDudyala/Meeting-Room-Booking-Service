package com.MRBS.service;

import java.util.List;

import com.MRBS.DTO.RoomRequest;
import com.MRBS.model.Room;

public interface RoomService {
	RoomRequest CreateRoom(RoomRequest req);
	List<Room> getRooms(Integer minCapacity, String amenity);
}
