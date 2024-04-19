package org.example.services;

import org.example.dtos.ResponseDTO;
import org.example.dtos.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseDTO<UserDTO> findById(String userId);
    ResponseDTO<String> update(UserDTO userDTO, MultipartFile avatar) throws Exception;
}
