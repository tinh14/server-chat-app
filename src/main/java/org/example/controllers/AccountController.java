package org.example.controllers;

import org.example.dtos.AccountDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.requests.ChangePasswordRequest;
import org.example.requests.DeleteAccountRequest;
import org.example.requests.SignupRequest;
import org.example.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/signin")
    public ResponseEntity<ResponseDTO<UserDTO>> signIn(@RequestBody AccountDTO accountDTO) {
        try {
            return ResponseEntity.ok(accountService.signIn(accountDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<UserDTO>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<UserDTO>> signUp(@RequestBody SignupRequest request) {
        try {
            return ResponseEntity.ok(accountService.signUp(request));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<UserDTO>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/accounts")
    public ResponseEntity<ResponseDTO<Void>> changePassword
            (@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            return ResponseEntity.ok(accountService.changePassword(changePasswordRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<Void>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/accounts")
    public ResponseEntity<ResponseDTO<Void>> deleteAccount(@RequestBody DeleteAccountRequest deleteAccountRequest) {
        try {
            return ResponseEntity.ok(accountService.deleteAccount(deleteAccountRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.<Void>builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
