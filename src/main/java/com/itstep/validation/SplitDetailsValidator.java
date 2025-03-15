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
import java.util.Objects;


public class SplitDetailsValidator implements ConstraintValidator<SplitDetailsConstraint, ExpenseSubmissionDto> {

    @Override
    public boolean isValid(ExpenseSubmissionDto expenseSubmissionDto, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (!validateSplitTypeNotNull(expenseSubmissionDto.getSplitType(), context)) {
            return false;
        }

        SplitType SPLIT_TYPE = SplitType.get(expenseSubmissionDto.getSplitType());
        List<SplitDetailsDto> splitDetails = expenseSubmissionDto.getSplitDetails();

        List<Boolean> isValid = new ArrayList<>();

        switch (SPLIT_TYPE) {
            case EQUAL:
                isValid.add(validateSplitDetailsNotNull(SPLIT_TYPE, splitDetails, context));
                isValid.add(validateUserNotNull(SPLIT_TYPE, splitDetails, context));
                break;
            case SHARES, MANUAL:
                isValid.add(validateSplitDetailsNotNull(SPLIT_TYPE, splitDetails, context));
                isValid.add(validateUserNotNull(SPLIT_TYPE, splitDetails, context));
                isValid.add(validateValueNotNull(SPLIT_TYPE, splitDetails, context));
                break;
            case PERCENTAGE:
                isValid.add(validateSplitDetailsNotNull(SPLIT_TYPE, splitDetails, context));
                isValid.add(validatePercentageSplit(SPLIT_TYPE, splitDetails, context));
                isValid.add(validateUserNotNull(SPLIT_TYPE, splitDetails, context));
                isValid.add(validateValueNotNull(SPLIT_TYPE, splitDetails, context));
                break;
            case BY_ITEM:
                isValid.add(validateByItem(expenseSubmissionDto, context));
                break;
        }
        return isValid.stream().allMatch(i -> i);
    }

    private boolean validateSplitTypeNotNull(String splitType, ConstraintValidatorContext context) {
        if (splitType == null) {
            context.buildConstraintViolationWithTemplate("Split type is required")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateUserNotNull(SplitType SPLIT_TYPE, List<SplitDetailsDto> splitDetails,
                                        ConstraintValidatorContext context) {
        boolean isValid = splitDetails.stream().noneMatch(item -> item.getUserName() == null);
        if (!isValid) {
            context.buildConstraintViolationWithTemplate("Missing User for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
        }
        return isValid;
    }

    private boolean validateValueNotNull(SplitType SPLIT_TYPE, List<SplitDetailsDto> splitDetails,
                                         ConstraintValidatorContext context) {
        boolean isValid = splitDetails.stream().noneMatch(item -> item.getValue() == null);
        if (!isValid) {
            context.buildConstraintViolationWithTemplate("Missing Value for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
        }
        return isValid;
    }

    private boolean validateSplitDetailsNotNull(SplitType SPLIT_TYPE, List<SplitDetailsDto> splitDetails,
                                                ConstraintValidatorContext context) {
        if (splitDetails == null || splitDetails.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Missing Split Details for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateItemsNotNull(List<ItemDto> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
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
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum)
                .compareTo(100.0) == 0;

        if (!isValid) {
            context.buildConstraintViolationWithTemplate("Percentage sum is not 100% for Split Type: " + SPLIT_TYPE.type)
                    .addConstraintViolation();
        }

        return isValid;
    }

    private boolean validateItemTotalPriceNotNull(ItemDto item, ConstraintValidatorContext context) {
        if (item.getTotalPrice() == null) {
            context.buildConstraintViolationWithTemplate("Missing item total price")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateByItem(ExpenseSubmissionDto expenseSubmissionDto, ConstraintValidatorContext context) {
        List<ItemDto> items = expenseSubmissionDto.getItems();

        if (!validateItemsNotNull(items, context)) {
            return false;
        }

        List<Boolean> isValid = new ArrayList<>();

        for (ItemDto item : items) {
            validateItemTotalPriceNotNull(item, context);

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
                    isValid.add(validateUserNotNull(SPLIT_TYPE, splitDetails, context));
                    isValid.add(validateValueNotNull(SPLIT_TYPE, splitDetails, context));
                    break;
                case PERCENTAGE:
                    isValid.add(validatePercentageSplit(SPLIT_TYPE, splitDetails, context));
                    isValid.add(validateUserNotNull(SPLIT_TYPE, splitDetails, context));
                    isValid.add(validateValueNotNull(SPLIT_TYPE, splitDetails, context));
                    break;
                default:
                    throw new NonExistingSplitType("Non existing split type");
            }
        }

        return isValid.stream().allMatch(i -> i);
    }
}
