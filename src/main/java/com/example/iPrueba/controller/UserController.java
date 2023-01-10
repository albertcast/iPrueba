package com.example.iPrueba.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.iPrueba.document.Comment;
import com.example.iPrueba.document.Message;
import com.example.iPrueba.document.User;
import com.example.iPrueba.repository.CommentRepository;
import com.example.iPrueba.repository.MessageRepository;
import com.example.iPrueba.repository.UserRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository mensajeRepository;
    @Autowired
    private CommentRepository comentarioRepository;
	private String cloud_name = "dazcrlzub";
	private String cloud_ak = "589373914839945";
	private String cloud_as = "HPtGK-hYaxWzREnHoELcHIucbdQ";
   
	
    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable String id) {
        return userRepository.findById(id);
    }
    
    @GetMapping(value = "/usersByAccountAuthentication", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> findByAccountAuthentication(@RequestParam("accountAuthentication") String accountAuthentication) {
        return userRepository.findByAccountAuthentication(accountAuthentication);
    }

    @GetMapping(value = "/usersByEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all users by email", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
	public Collection<User> getAllUsersByEmail(
        @Parameter(description = "email", required = true) @RequestParam("email") final String email) {
		return userRepository.findByEmailContainsIgnoreCase(email);
	}
    
    @GetMapping(value = "/usersByName", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all users by name", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
	public Collection<User> getAllUsersByName(
        @Parameter(description = "name", required = true) @RequestParam("name") final String name) {
		return userRepository.findByNameContainsIgnoreCase(name);
	}

    @GetMapping(value = "/usersByLastname", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all users by lastname", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
	public Collection<User> getAllUsersByLastname(
        @Parameter(description = "lastname", required = true) @RequestParam("lastname") final String lastname) {
		return userRepository.findByLastnameContainsIgnoreCase(lastname);
	}

    @GetMapping(value = "/usersByChats", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all persons that chat with a specific user", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
	public Collection<User> getAllUsersChatting(
        @Parameter(description = "usuarioId", required = true) @RequestParam("usuarioId") final String usuarioId) {
        List<Message> userMessages = new ArrayList<>();
        userMessages.addAll(mensajeRepository.findBySender(usuarioId));
        userMessages.addAll(mensajeRepository.findByReceiver(usuarioId));
        List<String> userIds = new ArrayList<>();
        List<User> usersChatting = new ArrayList<>();
        
        for(Message m : userMessages) {
            String sender = m.getSender();
            String receiver = m.getReceiver();
            if(sender.equals(usuarioId) && !userIds.contains(receiver)) {
                userIds.add(receiver);
                usersChatting.add(userRepository.findById(receiver).get());
            } else if (receiver.equals(usuarioId) && !userIds.contains(sender)){
                userIds.add(sender);
                usersChatting.add(userRepository.findById(sender).get());
            }
        }
        
        return usersChatting;

	}

    @GetMapping(value = "/usersByCommentInFlat", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all users that have sent at least one comment for a specific flat", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
	public Collection<User> getAllCommentsUsersByFlat(
        @Parameter(description = "vivienda", required = true) @RequestParam("vivienda") final String vivienda) {
        List<User> commentUsers = new ArrayList<>();
        List<String> commentUsersIds = new ArrayList<>();
        List<Comment> flatComments = comentarioRepository.findByApartment(vivienda).get();

        for(Comment c : flatComments) {
            String user = c.getUser();
            if(!commentUsersIds.contains(user)) {
                commentUsersIds.add(user);
                commentUsers.add(this.findById(user).get());
            }
        }

        return commentUsers;

	}

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "List all users", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
	public Collection<User> getAll() {
		return userRepository.findAll();
	}
    
    
    @PostMapping(value = "/")
    public ResponseEntity<User> addUser(
    		@Parameter(description = "email") @RequestParam("email") final String email,
            @Parameter(description = "name") @RequestParam("name") final String name,
            @Parameter(description = "lastname") @RequestParam("lastname") final String lastname,
            @Parameter(description = "age") @RequestParam("age") final Integer age,
            @Parameter(description = "accountAuthentication", required = true) @RequestParam("accountAuthentication") final String accountAuthentication,
    		@Parameter(description = "Image", required = false) @RequestParam("image") String image) throws ParseException, IOException {
    	if(findByAccountAuthentication(accountAuthentication).isEmpty()) {
    		
	        User user = userRepository.insert(new User(email, name, lastname, age, accountAuthentication, image, false));
	        return ResponseEntity.ok(user);
    	} else {
    		return null;
    	}
    	
    }
    
    
    @PutMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
        @Parameter(description = "id", required = true) @RequestParam("id") final String id,
        @Parameter(description = "email", required = false) @RequestParam("email") final String email,
        @Parameter(description = "name", required = false) @RequestParam("name") final String name,
        @Parameter(description = "lastname", required = false) @RequestParam("lastname") final String lastname,
        @Parameter(description = "age", required = false) @RequestParam("age") final Integer age,
        @Nullable @Parameter(description = "Image", required = false) @RequestParam("image") MultipartFile image) throws ParseException, IOException  {
            Optional<User> userOpt = userRepository.findById(id);
            if(!userOpt.isEmpty()) {
                User user = userOpt.get();
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
	                user.setImage(imString);

                }
                user.setEmail(email);
                user.setNombre(name);
                user.setApellidos(lastname);
                user.setEdad(age);
                user = userRepository.save(user);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    
    @DeleteMapping(value = "/")
    public void deleteUser(@RequestParam("id") final String id) {
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
    }
    

}
