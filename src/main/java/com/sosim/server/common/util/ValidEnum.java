package com.sosim.server.common.util;

import com.sosim.server.group.CoverColorType;
import com.sosim.server.group.GroupType;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Constraint(validatedBy = ValidEnum.EnumValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {

    String message() default "입력값 중 검증에 실패한 값이 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends EnumValidAble> target();
    Class<? extends Payload>[] payload() default {};

    class EnumValidator implements ConstraintValidator<ValidEnum, String> {
        private List<String> labels;

        @Override
        public void initialize(ValidEnum constraintAnnotation) {
            labels = new ArrayList<>();
            EnumValidAble[] target = constraintAnnotation.target().getEnumConstants();

            for (EnumValidAble groupType : target) {
                labels.add(groupType.getLabel());
            }
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return labels.contains(value);
        }
    }
}