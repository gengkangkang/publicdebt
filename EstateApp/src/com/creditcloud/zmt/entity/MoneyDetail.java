package com.creditcloud.zmt.entity;

import java.util.List;

public class MoneyDetail {
	
	private String date;
	
	private List<Event> events;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	

}
