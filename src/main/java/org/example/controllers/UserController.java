package org.example.controllers;

import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.services.UserService;
import org.example.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/v2")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<UserDTO>> findById(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(userService.findById(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<UserDTO>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @PatchMapping(path = "/users", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    public ResponseEntity<ResponseDTO<String>> update(
            @RequestPart(value = "user", name = "user") UserDTO dto,
            @RequestPart(value = "avatar", name = "avatar" , required = false) MultipartFile avatar) {

        try {
            return ResponseEntity.ok(userService.update(dto, avatar));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<String>builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
