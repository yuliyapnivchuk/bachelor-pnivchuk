package com.itstep.validation;

import com.itstep.dto.ExpenseSubmissionDto;
import com.itstep.dto.ItemDto;
import com.itstep.dto.SplitDetailsDto;
import com.itstep.exception.NonExistingSplitType;
import com.itstep.service.SplitType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;


public class SplitDetailsValidator implements ConstraintValidator<SplitDetailsConstraint, ExpenseSubmissionDto> {

    @Override
    public boolean isValid(ExpenseSubmissionDto expenseSubmissionDto, ConstraintValidatorContext context) {
        SplitType SPLIT_TYPE = SplitType.get(expenseSubmissionDto.getSplitType());
        List<SplitDetailsDto> splitDetails = expenseSubmissionDto.getSplitDetails();

        switch (SPLIT_TYPE) {
            case EQUAL:
                return validateSplitDetailsNotNull(SPLIT_TYPE, splitDetails, context)
                        && validateUserNotNull(SPLIT_TYPE, splitDetails, context);

            case SHARES, MANUAL:
                return validateSplitDetailsNotNull(SPLIT_TYPE, splitDetails, context)
                        && validateUserNotNull(SPLIT_TYPE, splitDetails, context)
                        && validateValueNotNull(SPLIT_TYPE, splitDetails, context);

            case PERCENTAGE:
                return validateSplitDetailsNotNull(SPLIT_TYPE, splitDetails, context)
                        && validatePercentageSplit(SPLIT_TYPE, splitDetails, context)
                        && validateUserNotNull(SPLIT_TYPE, splitDetails, context)
                        && validateValueNotNull(SPLIT_TYPE, splitDetails, context);

            case BY_ITEM:
                return validateByItem(expenseSubmissionDto, context);

            default:
                throw new NonExistingSplitType("Non existing split type");
        }
    }

    private boolean validateUserNotNull(SplitType SPLIT_TYPE, List<SplitDetailsDto> splitDetails,
                                        ConstraintValidatorContext context) {
        boolean isValid = splitDetails.stream().noneMatch(item -> item.getUser() == null);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Missing User for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
        }
        return isValid;
    }

    private boolean validateValueNotNull(SplitType SPLIT_TYPE, List<SplitDetailsDto> splitDetails,
                                         ConstraintValidatorContext context) {
        boolean isValid = splitDetails.stream().noneMatch(item -> item.getValue() == null);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Missing Value for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
        }
        return isValid;
    }

    private boolean validateSplitDetailsNotNull(SplitType SPLIT_TYPE, List<SplitDetailsDto> splitDetails,
                                                ConstraintValidatorContext context) {
        if (splitDetails == null || splitDetails.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Missing Split Details for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateItemsNotNull(List<ItemDto> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Missing Items")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validatePercentageSplit(SplitType SPLIT_TYPE, List<SplitDetailsDto> splitDetails,
                                            ConstraintValidatorContext context) {
        boolean isValid = splitDetails.stream()
                .map(SplitDetailsDto::getValue)
                .reduce(0.0, Double::sum)
                .compareTo(100.0) == 0;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Percentage sum is not 100% for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
        }

        return isValid;
    }

    private boolean validateByItem(ExpenseSubmissionDto expenseSubmissionDto, ConstraintValidatorContext context) {
        List<ItemDto> items = expenseSubmissionDto.getItems();

        if (!validateItemsNotNull(items, context)) {
            return false;
        }

        List<Boolean> isValid = new ArrayList<>();

        for (ItemDto item : items) {
            SplitType SPLIT_TYPE = SplitType.get(item.getSplitType());
            List<SplitDetailsDto> splitDetails = item.getSplitDetails();

            if (!validateSplitDetailsNotNull(SPLIT_TYPE, splitDetails, context)) {
                continue;
            }

            switch (SPLIT_TYPE) {
                case EQUAL:
                    isValid.add(validateUserNotNull(SPLIT_TYPE, splitDetails, context));
                    break;

                case SHARES, MANUAL:
                    isValid.add(
                            validateUserNotNull(SPLIT_TYPE, splitDetails, context)
                                    && validateValueNotNull(SPLIT_TYPE, splitDetails, context)
                    );
                    break;

                case PERCENTAGE:
                    isValid.add(
                            validatePercentageSplit(SPLIT_TYPE, splitDetails, context)
                                    && validateUserNotNull(SPLIT_TYPE, splitDetails, context)
                                    && validateValueNotNull(SPLIT_TYPE, splitDetails, context)
                    );
                    break;

                default:
                    throw new NonExistingSplitType("Non existing split type");
            }
        }

        return isValid.stream().allMatch(i -> i);
    }
}
