package org.example.controllers;

import org.example.dtos.MessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;
import org.example.services.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v2")
public class TextMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TextMessageService textMessageService;

    @MessageMapping("/conversations/{conversationId}/textMessages")
    public void create(@DestinationVariable String conversationId, TextMessageDTO messageDTO) {
        String destination = String.format("/public/conversations/%s/messages", conversationId);

        ResponseDTO<MessageDTO> res;
        try{
            res = textMessageService.create(messageDTO);
        }catch (Exception e){
            res = ResponseDTO.<MessageDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }

        messagingTemplate.convertAndSend(destination, res);
    }

}
