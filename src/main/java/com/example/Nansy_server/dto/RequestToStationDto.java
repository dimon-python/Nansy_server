package com.example.Nansy_server.dto;

public class RequestToStationDto {
    private String recipient;
    private String request;

    public String getRecipient() {
        return recipient;
    }
    public void setToPC(String recipient) {
        this.recipient = recipient;
    }
    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }
}