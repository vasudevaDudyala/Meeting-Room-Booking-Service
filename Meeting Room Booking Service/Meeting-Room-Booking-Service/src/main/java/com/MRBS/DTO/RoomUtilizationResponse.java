package com.MRBS.DTO;

public class RoomUtilizationResponse {

    private Long roomId;
    private String roomName;
    private double totalBookingHours;
    private double utilizationPercent;
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public double getTotalBookingHours() {
		return totalBookingHours;
	}
	public void setTotalBookingHours(double totalBookingHours) {
		this.totalBookingHours = totalBookingHours;
	}
	public double getUtilizationPercent() {
		return utilizationPercent;
	}
	public void setUtilizationPercent(double utilizationPercent) {
		this.utilizationPercent = utilizationPercent;
	}

    
}