package com.final_10aeat.domain.manageArticle.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class ScheduleSizeValidator implements
    ConstraintValidator<ScheduleSizeMustBiggerThanZero, List<?>> {

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        return list != null && !list.isEmpty();
    }
}
