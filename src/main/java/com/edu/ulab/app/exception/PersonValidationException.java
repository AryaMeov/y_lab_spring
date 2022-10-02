package com.edu.ulab.app.exception;

import javax.validation.ValidationException;

public class PersonValidationException extends ValidationException {
    public PersonValidationException(String msg) {
        super("Person not valid: "+ msg);
    }
}
