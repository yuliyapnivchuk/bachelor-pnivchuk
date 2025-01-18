package com.itstep.exception;

public class SpeechCouldNotBeRecognized extends RuntimeException {
    public SpeechCouldNotBeRecognized(String message) {
        super(message);
    }
}