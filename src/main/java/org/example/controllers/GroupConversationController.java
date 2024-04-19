package org.example.controllers;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.GroupConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2")
public class GroupConversationController {

    @Autowired
    private GroupConversationService groupConversationService;

    @PostMapping(path = "/groupConversations", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDTO<ConversationDTO>> create(
            @RequestPart("conversation") GroupConversationDTO dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        try {
            return ResponseEntity.ok(this.groupConversationService.create(dto, avatar));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseDTO.<ConversationDTO>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PatchMapping(path = "/groupConversations", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseDTO<String>> update(
            @RequestPart("conversation") GroupConversationDTO dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        try {
            return ResponseEntity.ok(this.groupConversationService.update(dto, avatar));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseDTO.<String>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/groupConversations/{conversationId}/participants/{joinerId}")
    public ResponseEntity<ResponseDTO<ConversationDTO>> join(@PathVariable String conversationId, @PathVariable String joinerId){
        try {
            return ResponseEntity.ok(this.groupConversationService.joinConversation(conversationId, joinerId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseDTO.<ConversationDTO>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/groupConversations/{conversationId}/participants/{leaverId}")
    public ResponseEntity<ResponseDTO<Void>> leave(@PathVariable String conversationId, @PathVariable String leaverId){
        try {
            return ResponseEntity.ok(this.groupConversationService.leaveConversation(conversationId, leaverId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseDTO.<Void>builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
