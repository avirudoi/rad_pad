package com.avirudoi.radpad.module;

import java.text.SimpleDateFormat;

public class RentObject {

	private String rentId;
	private String street;
	private String city;
	private long startdate;
	private long enddate;


	public RentObject() {
		
	}

	public void setRentId(String id) {
		rentId = id;
	}

	public void setStreet (String street) { this.street = street; }

	public void setCity (String city) { this.city = city; }

	public void setStartDate(long startdate) {
		this.startdate = startdate;
	}

	public void setEndDate(long enddate) {
		this.enddate = enddate;
	}

	public String getRentId() {
		return rentId;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public long getStartDate() {
		return startdate;
	}

	public long getEndDate() {
		return enddate;
	}

	public String getFormatedStartDate() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
		String startDay = dateFormat.format(startdate);
		return startDay;
	}

	public String getformattedEndDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
		String endDay = dateFormat.format(enddate);
		return endDay;
	}

	public String getFullDate(){

		String date = "";

		if(this.getFormatedStartDate() != null && this.getformattedEndDate() != null) {

			date = this.getFormatedStartDate() + "- " + this.getformattedEndDate();
		}
	return date;
	}

}
