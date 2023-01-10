package com.example.iPrueba.controller;

import com.cloudinary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.iPrueba.document.Apartment;
import com.example.iPrueba.document.User;
import com.example.iPrueba.repository.ApartmentRepository;
import com.example.iPrueba.repository.UserRepository;

@RestController
@RequestMapping("/api/apartment")
public class ApartmentController {
	private String cloud_name = "dazcrlzub";
	private String cloud_ak = "589373914839945";
	private String cloud_as = "HPtGK-hYaxWzREnHoELcHIucbdQ";
    @Autowired
    private ApartmentRepository repository;
    @Autowired
    private UserRepository userRepository;
    String pattern = "MM-dd-yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    @GetMapping("/{id}")
    public Optional<Apartment> findById(@PathVariable String id) {
        return repository.findById(id);
    }

    @GetMapping(value = "/apartmentsByPlace", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all flats by place", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Apartment.class))), responseCode = "200") })
	public Collection<Apartment> getAllFlatsByPlace(
        @Parameter(description = "place", required = true) @RequestParam("place") final String place) {
		return repository.findByPlace(place);
	}

    @GetMapping(value = "/apartmentsByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all flats by date", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Apartment.class))), responseCode = "200") })
	public Collection<Apartment> getAllFlatsByDate(
        @Parameter(description = "date", required = true) @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") final Date date) {
            return repository.findByDate(date);
	    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "List all flats", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Apartment.class))), responseCode = "200") })
	public Collection<Apartment> getAllFlats() {
		return repository.findAll();
	}
    
    @GetMapping(value = "/apartmentsByOwner", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all flats by owner", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Apartment.class))), responseCode = "200") })
	public Collection<Apartment> getAllFlatsByOwner(
        @Parameter(description = "owner", required = true) @RequestParam("owner") final String owner) {
		return repository.findByOwner(owner);
	}
    @GetMapping(value = "/apartmentsByPrice", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all flats <= price", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Apartment.class))), responseCode = "200") })
	public Collection<Apartment> getAllFlatsByPrice(
        @Parameter(description = "price", required = true) @RequestParam("price") final int price) {
    	List<Apartment> lista = repository.findByPrice(price);
    	List<Apartment> lista2 = new ArrayList<Apartment>();
    	for(Apartment a: lista) {
    		if(a.getPrice()<=price) {
    			lista2.add(a);
    		}
    	}
    	return lista2;
	}

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Apartment> addFlat(
            @Parameter(description = "title", required = true) @RequestParam("title") final String title,
            @Parameter(description = "place", required = true) @RequestParam("place") final String place,
            @Parameter(description = "description", required = true) @RequestParam("description") final String description,
            @Parameter(description = "date", required = true) @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") final Date date,
            @Parameter(description = "owner", required = true) @RequestParam("owner") final String owner,
            @Parameter(description = "capacity", required = true) @RequestParam("capacity") final int capacity,
            @Parameter(description = "price", required = true) @RequestParam("price") final int price,
			@Parameter(description = "Image", required = false) @RequestParam("image") MultipartFile image,
            @Parameter(description = "latitude", required = true) @RequestParam("latitude") final float latitude,
            @Parameter(description = "longitude", required = true) @RequestParam("longitude") final float longitude) throws ParseException, IOException {
    	
    	Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
    			"cloud_name", cloud_name,
    			"api_key", cloud_ak,
    			"api_secret", cloud_as,
    			"secure", true));
    	
    	Map params = ObjectUtils.asMap(
    		    "public_id", image.getOriginalFilename(), 
    		    "resource_type", "image"         
    		);
    	String imString = cloudinary.uploader().upload(image.getBytes(), params).get("url").toString();
    	
    	
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Apartment apartment = repository.insert(new Apartment(title, place, description, calendar.getTime(), owner, capacity,price, imString, latitude, longitude));
        return ResponseEntity.ok(apartment);
    }

    @PutMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    
    public ResponseEntity<Apartment> updateFlat(
        @Parameter(description = "id", required = true) @RequestParam("id") final String id,
        @Parameter(description = "title", required = false) @RequestParam("title") final String title,
        @Parameter(description = "place", required = false) @RequestParam("place") final String place,
        @Parameter(description = "description", required = false) @RequestParam("description") final String description,
        @Parameter(description = "date", required = false) @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") final Date date,
        @Parameter(description = "capacity", required = true) @RequestParam("capacity") final int capacity,
        @Parameter(description = "price", required = true) @RequestParam("price") final int price,
		@Nullable @Parameter(description = "Image", required = false) @RequestParam("image") MultipartFile image,
        @Parameter(description = "latitude", required = true) @RequestParam("latitude") final float latitude,
        @Parameter(description = "longitude", required = true) @RequestParam("longitude") final float longitude) throws ParseException, IOException  {
            Optional<Apartment> apartmentOpt = repository.findById(id);
            if(!apartmentOpt.isEmpty()) {
            	
            	
                Apartment apartment = apartmentOpt.get();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                apartment.setDate(calendar.getTime());
                apartment.setTitle(title);
                apartment.setPlace(place);
                apartment.setDescription(description);
                apartment.setCapacity(capacity);
                apartment.setPrice(price);
                apartment.setLatitude(latitude);
                apartment.setLongitude(longitude);
                if(image != null) {
                	Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                			"cloud_name", cloud_name,
                			"api_key", cloud_ak,
                			"api_secret", cloud_as,
                			"secure", true));
                	
                	Map params = ObjectUtils.asMap(
                		    "public_id", image.getOriginalFilename(), 
                		    "resource_type", "image"         
                		);
                	String imString = cloudinary.uploader().upload(image.getBytes(), params).get("url").toString();
                	
                	apartment.setImage(imString);
                }
                apartment = repository.save(apartment);
                return ResponseEntity.ok(apartment);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    

    @DeleteMapping(value = "/")
    public void deleteFlat(@RequestParam("id") final String id) {
        Apartment apartment = repository.findById(id).get();
        repository.delete(apartment);
    }

}
