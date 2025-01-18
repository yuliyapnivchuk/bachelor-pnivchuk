package com.itstep.exception;

public class NonExistingSplitType extends RuntimeException {
    public NonExistingSplitType(String message) {
        super(message);
    }
}