package org.example.validators;

import org.example.requests.SignupRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignupRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SignupRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupRequest signupRequest = (SignupRequest) target;

        if (signupRequest.getUsername() == null || signupRequest.getUsername().isEmpty()) {
            errors.rejectValue("username", "username.empty",
                    "Username is empty");
            return;
        }

        if (signupRequest.getUsername().length() < 6 || signupRequest.getUsername().length() > 32) {
            errors.rejectValue("username", "username.length",
                    "Username must be between 6 and 32 characters");
            return;
        }

        if (signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty",
                    "Password is not empty");
            return;
        }

        if (signupRequest.getPassword().length() < 6 || signupRequest.getPassword().length() > 32) {
            errors.rejectValue("password", "password.length",
                    "Password must be between 6 and 32 characters");
            return;
        }

        if (signupRequest.getConfirmPassword() == null || signupRequest.getConfirmPassword().isEmpty()) {
            errors.rejectValue("confirmPassword", "confirmPassword.empty",
                    "Confirm password is empty");
            return;
        }

        if (signupRequest.getConfirmPassword().length() < 6 || signupRequest.getConfirmPassword().length() > 32) {
            errors.rejectValue("password", "password.length",
                    "Confirm password must be between 6 and 32 characters");
        }
    }
}
