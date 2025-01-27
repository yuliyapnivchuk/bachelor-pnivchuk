package com.itstep.controller;

import com.itstep.dto.ExpenseErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExpenseRestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExpenseErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {

        List<String> allErrorMsg = new ArrayList<>();

        e.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage() + ";")
                .forEach(allErrorMsg::add);

        e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage() + ";")
                .forEach(allErrorMsg::add);

        ExpenseErrorResponse expenseErrorResponse = new ExpenseErrorResponse();
        expenseErrorResponse.setErrors(allErrorMsg);
        expenseErrorResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(expenseErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
