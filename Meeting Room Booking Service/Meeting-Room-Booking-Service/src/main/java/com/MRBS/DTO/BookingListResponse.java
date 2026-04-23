package com.MRBS.DTO;

import java.util.List;

public class BookingListResponse {

    private List<BookingRequest> items;
    private int total;
    private int limit;
    private int offset;
	public List<BookingRequest> getItems() {
		return items;
	}
	public void setItems(List<BookingRequest> items) {
		this.items = items;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}

   
}