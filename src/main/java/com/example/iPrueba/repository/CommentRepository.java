package com.example.iPrueba.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.iPrueba.document.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment,String> {

    Optional<List<Comment>> findByApartment(String apartment);

    Optional<List<Comment>> findByUser(String user);


}
