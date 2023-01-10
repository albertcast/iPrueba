package com.example.iPrueba.repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.iPrueba.document.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {
	public List<Booking> findByGuest(String guest);
	public List<Booking> findByArrivalDate(LocalDate arrivalDate);
	public List<Booking> findByDepartureDate(LocalDate departureDate);
	public List<Booking> findByApartment(String apartment);
	public Optional<Booking> findById(String id);
	
}
