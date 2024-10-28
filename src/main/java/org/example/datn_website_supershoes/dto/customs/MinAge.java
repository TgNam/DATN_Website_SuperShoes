package org.example.datn_website_supershoes.dto.customs;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {
    int value();

    String message() default "Bạn phải ít nhất {value} tuổi";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


