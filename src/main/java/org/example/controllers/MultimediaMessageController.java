package org.example.controllers;

import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.example.dtos.MessageDTO;
import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.MultimediaMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2")
public class MultimediaMessageController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MultimediaMessageService multimediaMessageService;

    @PostMapping("/multimediaMessages")
    public ResponseEntity<ResponseDTO<Void>> create(
            @RequestPart("message") MultimediaMessageDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        String destination = String.format("/public/conversations/%s/messages", dto.getConversationId());

        try {
            ResponseDTO<MessageDTO> res = multimediaMessageService.create(dto, file);

            messagingTemplate.convertAndSend(destination, res);

            return ResponseEntity.ok(ResponseDTO.<Void>builder()
                    .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<Void>builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
