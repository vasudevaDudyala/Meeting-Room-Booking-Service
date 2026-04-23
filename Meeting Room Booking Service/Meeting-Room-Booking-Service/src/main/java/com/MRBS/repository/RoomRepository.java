package com.MRBS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MRBS.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{
    Optional<Room> findByNameIgnoreCase(String name);
    List<Room> findByCapacityGreaterThanEqual(int capacity);
    List<Room> findByAmenitiesContaining(String amenity);
    List<Room> findByCapacityGreaterThanEqualAndAmenitiesContaining(int capacity, String amenity);

}
