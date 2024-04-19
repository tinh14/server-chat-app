package org.example.validators;

import org.example.requests.ChangePasswordRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordRequest changePasswordRequest = (ChangePasswordRequest) target;

        if (changePasswordRequest.getOldPassword() == null || changePasswordRequest.getOldPassword().isEmpty()) {
            errors.rejectValue("oldPassword", "oldPassword.empty",
                    "Old password is empty");
            return;
        }

        if (changePasswordRequest.getOldPassword().length() < 6 || changePasswordRequest.getOldPassword().length() > 32) {
            errors.rejectValue("oldPassword", "oldPassword.length",
                    "Old password must be between 6 and 32 characters");
            return;
        }

        if (changePasswordRequest.getNewPassword() == null || changePasswordRequest.getNewPassword().isEmpty()) {
            errors.rejectValue("newPassword", "newPassword.empty",
                    "New password is empty");
            return;
        }

        if (changePasswordRequest.getNewPassword().length() < 6 || changePasswordRequest.getNewPassword().length() > 32) {
            errors.rejectValue("newPassword", "newPassword.length",
                    "New password must be between 6 and 32 characters");
            return;
        }

        if (changePasswordRequest.getConfirmNewPassword() == null || changePasswordRequest.getConfirmNewPassword().isEmpty()) {
            errors.rejectValue("confirmNewPassword", "confirmNewPassword.empty",
                    "Confirm new password is empty");
            return;
        }

        if (changePasswordRequest.getConfirmNewPassword().length() < 6 || changePasswordRequest.getConfirmNewPassword().length() > 32) {
            errors.rejectValue("confirmNewPassword", "confirmNewPassword.length",
                    "Confirm new password must be between 6 and 32 characters");
        }
    }
}
