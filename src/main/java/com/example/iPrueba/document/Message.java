package com.example.iPrueba.document;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Message {
    @Id
    private String id;
    private String sender;
    private String receiver;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;    
    public Message() {}

    public Message(String sender, String receiver, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return this.timestamp;
    }

    public void setDate(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", remitente='" + getSender() + "'" +
            ", destinatario='" + getReceiver() + "'" +
            ", contenido='" + getContent() + "'" +
            "}";
    }
    
}
