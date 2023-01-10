package com.example.iPrueba.controller;

import com.example.iPrueba.document.Apartment;
import com.example.iPrueba.document.Comment;
import com.example.iPrueba.document.User;
import com.example.iPrueba.repository.ApartmentRepository;
import com.example.iPrueba.repository.CommentRepository;
import com.example.iPrueba.repository.UserRepository;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    private List<Comment> findAll(){
    	return commentRepository.findAll();
        
    }

    @GetMapping("/{id}")
    private ResponseEntity<Comment> findById(@PathVariable final String id){
        try{
            return new ResponseEntity<>(commentRepository.findById(id).get(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/commentsByApartment")
    private ResponseEntity<List<Comment>> findByApartment(@RequestParam final String id){
        try{
            return new ResponseEntity<>(commentRepository.findByApartment(id).get(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/commentsByUser")
    private ResponseEntity<List<Comment>> findByUser(@RequestParam final String id){
        try{
            return new ResponseEntity<>(commentRepository.findByUser(id).get(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    private ResponseEntity<Comment> addComment(
            @Parameter(description = "user", required = true) @RequestParam("user") final String user,
            @Parameter(description = "apartment", required = true) @RequestParam("apartment") final String apartment,
            @Parameter(description = "text", required = true) @RequestParam("text") final String text,
            @Parameter(description = "rating", required = true) @RequestParam("rating") final String rating,
            @Parameter(description = "date", required = true) @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate date){
        try{
            String ap = apartmentRepository.findById(apartment).get().getId();
            String usr = userRepository.findById(user).get().getUsuarioId();
            Comment comment;

            if(ap == null || usr == null) { throw new Exception();}
            comment = new Comment(text,rating,usr,ap,date);
            commentRepository.insert(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/")
    public ResponseEntity<Comment> updateComment(
            @Parameter(description = "id", required = true) @RequestParam("id") final String id,
            @Parameter(description = "user", required = true) @RequestParam("user") final String user,
            @Parameter(description = "apartment", required = true) @RequestParam("apartment") final String apartment,
            @Parameter(description = "text", required = true) @RequestParam("text") final String text,
            @Parameter(description = "rating", required = true) @RequestParam("rating") final String rating) throws ParseException {
        Optional<Comment> commentOpt = commentRepository.findById(id);
        if(!commentOpt.isEmpty()) {
            Comment comment = commentOpt.get();
            comment.setUser(user);
            comment.setApartment(apartment);
            comment.setText(text);
            comment.setRating(rating);
            comment = commentRepository.save(comment);
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/")
    private void deleteComment(@RequestParam final String id){
        commentRepository.deleteById(id);
    }


}
