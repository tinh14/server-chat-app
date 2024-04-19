package org.example.controllers;


import org.example.dtos.MessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v2")
public class MessageController {

    @Autowired
    private MessageService textMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;



}
