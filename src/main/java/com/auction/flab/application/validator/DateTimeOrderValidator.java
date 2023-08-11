package com.auction.flab.application.validator;

import com.auction.flab.application.exception.ErrorCode;
import com.auction.flab.application.exception.ProjectException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DateTimeOrderValidator implements ConstraintValidator<DateTimeOrder, Object> {

    private String prevDateTime;
    private String laterDateTime;

    @Override
    public void initialize(DateTimeOrder constraintAnnotation) {
        prevDateTime = constraintAnnotation.previousDateTime();
        laterDateTime = constraintAnnotation.laterDateTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        LocalDateTime prevDateTime = getFieldValue(value, this.prevDateTime);
        LocalDateTime laterDateTime = getFieldValue(value, this.laterDateTime);
        return (prevDateTime != null && laterDateTime != null) && laterDateTime.isAfter(prevDateTime);
    }

    private LocalDateTime getFieldValue(Object object, String fieldName)  {
        Field field;
        try {
            field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (LocalDateTime) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ProjectException(ErrorCode.EXCEPTION_ON_EXTRACT_FIELD_VALUE);
        }
    }

}
