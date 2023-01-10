package com.example.iPrueba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.iPrueba.document.Message;

public interface MessageRepository extends MongoRepository<Message, String> {

    public Optional<Message> findById(String id);
    public List<Message> findBySender(String sender);
    public List<Message> findByReceiver(String receiver);
    public List<Message> findBySenderAndReceiver(String sender, String receiver);
    
}
