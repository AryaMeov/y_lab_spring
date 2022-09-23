package com.edu.ulab.app.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User not found by id: "+ id);
    }
}
