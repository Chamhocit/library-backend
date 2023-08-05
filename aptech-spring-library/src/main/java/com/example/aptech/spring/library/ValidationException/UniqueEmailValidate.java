package com.example.aptech.spring.library.ValidationException;

import com.example.aptech.spring.library.Service.UniqueEmail;
import com.example.aptech.spring.library.dao.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidate implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && !userRepository.existsUserByEmail(email);
    }
}
