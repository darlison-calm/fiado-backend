package com.fiado.domain.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhone, PhoneNumberEntity> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(@NotNull PhoneNumberEntity phoneNumberEntity, ConstraintValidatorContext context) {
        if (phoneNumberEntity.getLocale() == null || phoneNumberEntity.getValue() == null) {
            return false;
        }
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumberEntity.getValue(), phoneNumberEntity.getLocale()));
        } catch (NumberParseException e) {
            return false;
        }
    }
}