package com.example.Nansy_server.dto;

import org.springframework.messaging.handler.annotation.Payload;

public class CommandRequestDto {
    private String commandId;
    private String recipient;
    private String sender;
    private String action;
    private Payload payload;

    public String getCommandId() {
        return commandId;
    }
    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    
    public Payload getPayload() {
        return payload;
    }
    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}