package com.edu.ulab.app.exception;

public class PersonNotFoundException extends NotFoundException {
    public PersonNotFoundException(Long id) {
        super("Person not found by id: "+ id);
    }
}
