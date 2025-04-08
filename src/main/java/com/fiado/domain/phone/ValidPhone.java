package com.fiado.domain.phone;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String locale() default "";

    String message() default "Celular é inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}