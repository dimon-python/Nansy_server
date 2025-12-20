package com.example.Nansy_server.сontroller;

import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@Controller
public class ChatController {
    @MessageMapping("/echo")
    @SendTo("/topic/echo")
    public String echo(String message) {
        System.out.println("Сообщение: " + message);
        return message;
    }
}
