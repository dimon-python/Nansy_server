package com.example.Nansy_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
public class RequestToStationService { // Station = компьютер пользователя
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    public void requestToStation(String recipient, String request) {
       messagingTemplate.convertAndSendToUser(recipient, "/queue/requests", request);
    }

    public boolean isRecipientConnected(String user) {
        SimpUser recipient = simpUserRegistry.getUser(user);
        return recipient != null;
    }

    public void errorStationNotConnected(String senderName) {
        messagingTemplate.convertAndSendToUser(senderName, "/queue/errors", "303");
    }
}