package com.example.iPrueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.iPrueba.document.Apartment;
import com.example.iPrueba.document.Booking;
import com.example.iPrueba.document.Comment;
import com.example.iPrueba.repository.BookingRepository;



@RestController
@RequestMapping("/api/booking")
public class BookingController {
	public class DateComparator implements Comparator<Booking> {
	    @Override
	    public int compare(Booking o1, Booking o2) {
	        return o1.arrivalDate.compareTo(o2.arrivalDate);
	    }
	}
	
	@Autowired
    private BookingRepository repository;
	
	
	@Operation(description = "Find booking by guest", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = Booking.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Booking with such guest doesn't exists") })
	@GetMapping(value = "/bookingsByGuest", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Booking> findByGuest(@RequestParam String guest) {
        List<Booking> lista = repository.findByGuest(guest);
        lista.sort(new DateComparator().reversed());
        return lista;
    }
	
	@Operation(description = "Find booking by arrival date", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = Booking.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Booking with such arrival date doesn't exists") })
	@GetMapping(value = "/bookingsByArrivalDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Booking> findByArrivalDate(@Parameter(description = "date", required = true) @RequestParam("arrivalDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate arrivalDate) throws ParseException {
        List<Booking> lista = repository.findByArrivalDate(arrivalDate);
        return lista;
    }
	
	@Operation(description = "Find booking by departure date", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = Booking.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Booking with such departure date doesn't exists") })
	@GetMapping(value = "/bookingsByDepartureDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Booking> findByDepartureDate(@Parameter(description = "date", required = true) @RequestParam("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate departureDate) throws ParseException {
        List<Booking> lista = repository.findByDepartureDate(departureDate);
        return lista;
    }
	
	@Operation(description = "Find booking by apartment", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = Booking.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Booking with such apartment doesn't exists") })
	@GetMapping(value = "/bookingsByApartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Booking> findByApartment(@RequestParam String apartment) {
        List<Booking> lista = repository.findAll();
        List<Booking> lista2 = new ArrayList<Booking>();
        for(Booking b : lista) {
        	if(b.apartment.toLowerCase().contains(apartment.toLowerCase())) {
        		lista2.add(b);
        	}
        }
        return lista2;
    }
	
	@GetMapping("/{id}")
    public Optional<Booking> findById(@PathVariable String id) {
        return repository.findById(id);
    }
	
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "List all bookings", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Booking.class))), responseCode = "200") })
	public Collection<Booking> getPeople() {
		return repository.findAll();
	}
	
	@PostMapping(value = "/")
    public ResponseEntity<Booking> addBooking(
            @Parameter(description = "guest", required = true) @RequestParam("guest") final String guest,
            @Parameter(description = "arrivalDate", required = true) @RequestParam("arrivalDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate arrivalDate,
            @Parameter(description = "departureDate", required = true) @RequestParam("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate departureDate,
            @Parameter(description = "apartment", required = true) @RequestParam("apartment") final String apartment) throws ParseException {
        Booking booking = repository.insert(new Booking(guest, arrivalDate, departureDate, apartment));
        return ResponseEntity.ok(booking);
    }
	
	@PutMapping(value = "/")
    public ResponseEntity<Booking> updateBooking(
    		@Parameter(description = "id", required = true) @RequestParam("id") final String id,
    		@Parameter(description = "guest", required = true) @RequestParam("guest") final String guest,
            @Parameter(description = "arrivalDate", required = true) @RequestParam("arrivalDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate arrivalDate,
            @Parameter(description = "departureDate", required = true) @RequestParam("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate departureDate,
            @Parameter(description = "apartment", required = true) @RequestParam("apartment") final String apartment) throws ParseException  {
            Optional<Booking> bookingOpt = repository.findById(id);
            if(!bookingOpt.isEmpty()) {
                Booking booking = bookingOpt.get();
                booking.setArrivalDate(arrivalDate);
                booking.setDepartureDate(departureDate);
                booking.setGuest(guest);
                booking.setApartment(apartment);
                booking = repository.save(booking);
                return ResponseEntity.ok(booking);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
	@DeleteMapping(value = "/")
    public void deleteBooking(@RequestParam("id") final String id) {
        Booking booking = repository.findById(id).get();
        repository.delete(booking);
    }

}

