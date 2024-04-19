package org.example.controllers;

import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationReadStatusEntity;
import org.example.services.ConversationReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ConversationReadStatusController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ConversationReadStatusService conversationReadStatusService;


    @MessageMapping("/user/{userId}/conversationReadStatus/{conversationId}")
    public void markNewMessagesAsRead(
            @DestinationVariable String userId,
            @DestinationVariable String conversationId) {
        String destination = String.format("/private/user/%s/conversationReadStatus", userId);

        try {
            messagingTemplate.convertAndSend(destination, conversationReadStatusService.markNewMessagesAsRead(userId, conversationId));
        }catch (Exception e) {
            messagingTemplate.convertAndSend(destination, ResponseDTO.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
