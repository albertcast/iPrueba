package com.example.iPrueba.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.iPrueba.document.Apartment;

public interface ApartmentRepository extends MongoRepository<Apartment, String> {

    public Optional<Apartment> findById(String id);
    public List<Apartment> findByPlace(String place);
    public List<Apartment> findByDate(Date date);
    public List<Apartment> findByOwner(String owner);
    public List<Apartment> findByPrice(int price);

}
