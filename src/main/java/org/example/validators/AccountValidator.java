package org.example.validators;

import org.example.dtos.AccountDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) target;

        if (accountDTO.getUsername() == null || accountDTO.getUsername().isEmpty()) {
            errors.rejectValue("username", "username.empty",
                    "Username is empty");
            return;
        }

        if (accountDTO.getUsername().length() < 6 || accountDTO.getUsername().length() > 32) {
            errors.rejectValue("username", "username.length",
                    "Username must be between 6 and 32 characters");
            return;
        }

        if (accountDTO.getPassword() == null || accountDTO.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty",
                    "Password is empty");
            return;
        }

        if (accountDTO.getPassword().length() < 6 || accountDTO.getPassword().length() > 32) {
            errors.rejectValue("password", "password.length",
                    "Password must be between 6 and 32 characters");
        }
    }
}
