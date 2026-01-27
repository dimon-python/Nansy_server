package com.example.Nansy_server.сontroller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.security.Principal;
import com.example.Nansy_server.dto.RequestToPC;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/requestToPC")
    public void requestToPC(@Payload RequestToPC request, Principal sender) {
        String toPC = request.getToPC();
        String requestToPC = request.getRequest();

    }
}
