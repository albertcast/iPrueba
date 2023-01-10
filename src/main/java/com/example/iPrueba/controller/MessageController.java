package com.example.iPrueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;

import com.example.iPrueba.document.Message;
import com.example.iPrueba.repository.MessageRepository;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    
    @Autowired
    private MessageRepository repository;

    @GetMapping("/{id}")
    public Optional<Message> findById(@PathVariable String id) {
        return repository.findById(id);
    }

    @GetMapping(value = "/messagesBySender", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all messages by one sender", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class))), responseCode = "200") })
	public Collection<Message> getAllSenderMessages(@RequestParam String userId) {
		return repository.findBySender(userId);
	}

    @GetMapping(value = "/messagesByReceiver", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all messages by one receiver", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class))), responseCode = "200") })
	public Collection<Message> getAllReceiverMessages(@RequestParam String userId) {
		return repository.findByReceiver(userId);
	}

    @GetMapping(value = "/messagesByUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all conversations by one user", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class))), responseCode = "200") })
	public Collection<Message> getAllUserMessages(@RequestParam String userId) {
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(this.getAllSenderMessages(userId));
        allMessages.addAll(this.getAllReceiverMessages(userId));
		return allMessages;
	}

    @GetMapping(value = "/messagesBetweenUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List a full conversation between two users", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class))), responseCode = "200") })
    public Collection<Message> findBySenderAndReceiver(@RequestParam String firstUserId, @RequestParam String secondUserId) {
        List<Message> fullConversation = new ArrayList<>();
        fullConversation.addAll(repository.findBySenderAndReceiver(firstUserId, secondUserId));
        fullConversation.addAll(repository.findBySenderAndReceiver(secondUserId, firstUserId));
        Collections.sort(fullConversation, new Comparator<Message>() {
            public int compare(Message m1, Message m2) {
                return m1.getDate().compareTo(m2.getDate());
            }
        });

        return fullConversation;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "List all messages", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class))), responseCode = "200") })
	public Collection<Message> getAllMessages() {
		return repository.findAll();
	}

    @PostMapping(value = "/")
    public ResponseEntity<Message> addMensaje(
            @Parameter(description = "sender", required = true) @RequestParam("sender") final String sender,
            @Parameter(description = "receiver", required = true) @RequestParam("receiver") final String receiver,
            @Parameter(description = "content", required = true) @RequestParam("content") final String content) {
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
		String formatDateTime = LocalDateTime.now().format(format);
        LocalDateTime ldt = LocalDateTime.parse(formatDateTime, format);
        
        Message mensaje = repository.insert(new Message(sender, receiver, content, ldt));
        return ResponseEntity.ok(mensaje);
    
    }

    @PutMapping(value = "/")
    public ResponseEntity<Message> updateMessage(
        @Parameter(description = "id", required = true) @RequestParam("id") final String id,
        @Parameter(description = "content", required = true) @RequestParam("content") final String content) {
            
            Optional<Message> mensajeOpt = repository.findById(id);
            if(!mensajeOpt.isEmpty()) {
                Message mensaje = mensajeOpt.get();
                mensaje.setContent(content);
                mensaje = repository.save(mensaje);
                return ResponseEntity.ok(mensaje);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    

    @DeleteMapping(value = "/")
    public void deleteMessage(@RequestParam("id") final String id) {
            Message mensaje = repository.findById(id).get();
            repository.delete(mensaje);
        }

}
