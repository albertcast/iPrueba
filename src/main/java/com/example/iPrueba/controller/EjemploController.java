package com.example.iPrueba.controller;

import com.example.iPrueba.document.Ejemplo;
import com.example.iPrueba.repository.EjemploRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.util.Collection;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/ejemplo")
public class EjemploController {

    @Autowired
    private EjemploRepository repository;

    
    
    @Operation(description = "Find ejemplo by abc", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = Ejemplo.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Ejemplo with such abc doesn't exists") })
	@GetMapping(value = "/{abc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Ejemplo findByAbc(@PathVariable String abc) {
        return repository.findByAbc(abc);
    }
    
    
    
    @GetMapping("/{id}")
    public Optional<Ejemplo> findById(@PathVariable String id) {
        return repository.findById(id);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "List all people", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ejemplo.class))), responseCode = "200") })
	public Collection<Ejemplo> getPeople() {
		return repository.findAll();
	}
    
    //POST SIMPLE, NO CONTROLA QUE SE METE
    @PostMapping(value="repository")
    public ResponseEntity<Ejemplo> addHola(Ejemplo ejemplo){
    	repository.insert(ejemplo);
    	return ResponseEntity.ok(ejemplo);
    }
    
    //POST POR PROBAR
    @PostMapping(value = "/{abc}")
	@Operation(description = "Create new Ejemplo", responses = {
			@ApiResponse(responseCode = "409", description = "Person with such e-mail already exists") })
	public ResponseEntity<Ejemplo> addEjemplo(
			@Parameter(description = "Abc", required = true) @PathVariable("abc") final String abc,
			@Parameter(description = "First Name", required = true) @RequestParam("firstName") final String firstName,
			@Parameter(description = "Last Name", required = true) @RequestParam("lastName") final String lastName) {

		final Ejemplo person = repository.findByAbc(abc);

		

		repository.insert(new Ejemplo(firstName, lastName, abc));
		
		return ResponseEntity.ok(person);
	}
    
}

