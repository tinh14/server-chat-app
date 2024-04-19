package org.example.validators;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class GroupConversationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ConversationDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GroupConversationDTO conversationDTO = (GroupConversationDTO) target;

        if (conversationDTO.getName() == null || conversationDTO.getName().isEmpty()) {
            errors.rejectValue("name", "name.empty",
                    "Name is empty");
            return;
        }

        if (conversationDTO.getName().length() < 6 || conversationDTO.getName().length() > 50) {
            errors.rejectValue("name", "name.length",
                    "Name must be between 6 and 50 characters");
            return;
        }
    }

    public void validate(GroupConversationDTO groupConversationDTO) throws Exception {
        Errors errors = new BeanPropertyBindingResult(
                groupConversationDTO, groupConversationDTO.getClass().getSimpleName());

        this.validate(groupConversationDTO, errors);

        if (errors.hasErrors()) {
            throw new RuntimeException(errors.getAllErrors().get(0).getDefaultMessage());
        }

        if (errors.hasErrors()) {
            throw new RuntimeException(errors.getAllErrors().get(0).getDefaultMessage());
        }

    }
}
