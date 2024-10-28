package org.example.datn_website_supershoes.dto.customs;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Calendar;
import java.util.Date;

public class MinAgeValidator implements ConstraintValidator<MinAge, Date> {
    private int minAge;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Date birthday, ConstraintValidatorContext context) {
        if (birthday == null) {
            return true; // Skip validation for null values, use @NotNull if required
        }

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(birthday);

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age >= minAge;
    }
}

