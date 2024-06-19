package com.final_10aeat.common.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class ListNotEmptyValidator implements
    ConstraintValidator<ListNotEmpty, List<?>> {

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        return list != null && !list.isEmpty();
    }
}
