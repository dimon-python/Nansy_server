package com.example.Nansy_server.сontroller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import java.security.Principal;
import com.example.Nansy_server.service.RequestToStationService;
import com.example.Nansy_server.dto.RequestToStationDto;

@Controller
public class ChatController {

    private final RequestToStationService requestToStationService;

    @Autowired
    public ChatController(RequestToStationService requestToStationService) {
        this.requestToStationService = requestToStationService;
    }

    @MessageMapping("/requestToPC")
    public void requestToPC(@Payload RequestToStationDto request, Principal sender) {
        String recipient = request.getRecipient();
        String requestToStation = request.getRequest();

        if (requestToStationService.isRecipientConnected(recipient)) {
            requestToStationService.requestToStation(recipient, requestToStation);
        } else {
            requestToStationService.errorStationNotConnected(sender.getName());
        }
    }
}
