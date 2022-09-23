package com.edu.ulab.app.exception;

import javax.validation.ValidationException;

public class UserValidationException extends ValidationException {
    public UserValidationException(String msg) {
        super("User not valid: "+ msg);
    }
}
