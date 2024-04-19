package org.example.controllers;

import org.example.dtos.ConversationDTO;
import org.example.dtos.MessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.ConversationService;
import org.example.services.MessageService;
import org.example.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/conversations/participants/{participantId}")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> findByParticipantId(@PathVariable String participantId) {
        try {
            return ResponseEntity.ok(conversationService.findAllByParticipantId(participantId));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<List<ConversationDTO>>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/conversations/{conversationId}/participants/{participantId}")
    public ResponseEntity<ResponseDTO<ConversationDTO>> findById(@PathVariable String conversationId, @PathVariable String participantId) {
        try {
            return ResponseEntity.ok(conversationService.findOneByConversationIdAndParticipantId(conversationId, participantId));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<ConversationDTO>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ResponseDTO<List<MessageDTO>>> findByConversationId(
            @PathVariable String conversationId, @RequestParam int page) {
        try {
            return ResponseEntity.ok(messageService.findByConversationId(conversationId, page));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<List<MessageDTO>>builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
