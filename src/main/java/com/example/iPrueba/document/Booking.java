package com.example.iPrueba.document;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;

public class Booking {
	@Id
	  public String id;

	  public String guest;
	  public LocalDate arrivalDate;
	  public LocalDate departureDate;
	  public String apartment;

	  public Booking() {}

	  public Booking(String guest, LocalDate arrivalDate, LocalDate departureDate, String apartment) {
	    this.guest = guest;
	    this.arrivalDate = arrivalDate;
	    this.departureDate = departureDate;
	    this.apartment = apartment;
	  }

	  @Override
	  public String toString() {
	    return String.format(
	        "Reserva[id=%s, guest='%s', arrivalDate='%s', departureDate='%s', apartment='%s']",
	        id, guest, arrivalDate, departureDate, apartment);
	  }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
}
