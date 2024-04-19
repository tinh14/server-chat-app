package org.example.validators;

import org.example.entities.MultimediaMessageEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validate(MultipartFile multipartFile) throws Exception {

        if (multipartFile == null){
            throw new RuntimeException("File is empty");
        }

        Errors errors = new BeanPropertyBindingResult(multipartFile, "multipartFile");

        this.validate(multipartFile, errors);

        if (errors.hasErrors()) {
            throw new RuntimeException(errors.getAllErrors().get(0).getDefaultMessage());
        }
    }
}
