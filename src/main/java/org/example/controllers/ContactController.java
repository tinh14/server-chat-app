package org.example.controllers;

import org.example.dtos.ContactDTO;
import org.example.dtos.ResponseDTO;
import org.example.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/participants/{userId}")
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> findAllByUserId(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(this.contactService.findAllByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.<List<ContactDTO>>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/contacts/received/participants/{userId}")
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> findReceivedContactRequests(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(this.contactService.findReceivedContactRequests(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.<List<ContactDTO>>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/contacts/sent/participants/{userId}")
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> findSentContactRequests(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(this.contactService.findSentContactRequests(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.<List<ContactDTO>>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/contacts")
    public ResponseEntity<ResponseDTO<String>> sendContactRequest(@RequestBody ContactDTO contactDTO) {
        try {
            return ResponseEntity.ok(this.contactService.sendContactRequest(contactDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.<String>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PatchMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseDTO<Void>> acceptContactRequest(@PathVariable String contactId) {
        try {
            return ResponseEntity.ok(this.contactService.acceptContactRequest(contactId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.<Void>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseDTO<Void>> deleteContactRequest(@PathVariable String contactId) {
        try {
            return ResponseEntity.ok(this.contactService.deleteContact(contactId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.<Void>builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
