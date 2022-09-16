package com.edu.ulab.app.exception;

import javax.validation.ValidationException;

public class BookValidationException extends ValidationException {
    public BookValidationException(String msg) {
        super("Book not valid: "+ msg);
    }
}
