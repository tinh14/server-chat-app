package org.example.validators;

import org.example.dtos.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validate(UserDTO userDTO) throws Exception {
        String errorMessage = null;

        if (userDTO.getName() == null || userDTO.getName().isBlank()) {
            errorMessage = "Name is empty";
            return;
        }

        if (userDTO.getName().length() < 6 || userDTO.getName().length() > 50) {
            errorMessage =  "Name must be between 6 and 50 characters";
        }

        if (errorMessage != null) {
            throw new RuntimeException(errorMessage);
        }
    }
}
