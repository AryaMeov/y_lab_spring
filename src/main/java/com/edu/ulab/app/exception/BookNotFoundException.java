package com.edu.ulab.app.exception;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(Long id) {
        super("Book not found by id: "+ id);
    }
}
