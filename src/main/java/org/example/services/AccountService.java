package org.example.services;

import org.example.dtos.AccountDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.example.requests.ChangePasswordRequest;
import org.example.requests.DeleteAccountRequest;
import org.example.requests.SignupRequest;

public interface AccountService {
    ResponseDTO<UserDTO> signIn(AccountDTO accountDTO) throws Exception;
    ResponseDTO<UserDTO> signUp(SignupRequest request) throws Exception;
    ResponseDTO<Void> changePassword(ChangePasswordRequest changePasswordRequest) throws Exception;
    ResponseDTO<Void> deleteAccount(DeleteAccountRequest deleteAccountRequest) throws Exception;
}
