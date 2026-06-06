package com.example.Nansy_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import com.example.Nansy_server.service.RequestToStationService;
import com.example.Nansy_server.dto.CommandRequestDto;

@Controller
public class WebSocketController {

    private final RequestToStationService requestToStationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(RequestToStationService requestToStationService, SimpMessagingTemplate messagingTemplate) {
        this.requestToStationService = requestToStationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/requestToPC")
    public void requestToPC(@Payload CommandRequestDto request, Principal sender) {
        String recipient = request.getRecipient();
        String action = request.getAction();

        if (requestToStationService.isRecipientConnected(recipient)) {
            requestToStationService.requestToStation(recipient, action);
        } else {
            requestToStationService.errorStationNotConnected(sender.getName());
        }
    }

    @MessageMapping("/echo")
    public void echo(String message) {
        messagingTemplate.convertAndSend("/topic/echo", message);
    }
}
