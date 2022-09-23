package com.edu.ulab.app.validation;

import com.edu.ulab.app.exception.BookValidationException;
import com.edu.ulab.app.exception.UserValidationException;
import com.edu.ulab.app.web.request.BookRequest;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.request.UserRequest;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
public class UserBookRequestValidationService {
    public void validateCreateRequest(UserBookRequest request) {
        validateRequest(request);
        if (request.getUserRequest().getId() != null) {
            throw new UserValidationException("User id in create request is not empty");
        }
        validateUserRequest(request.getUserRequest());
        request.getBookRequests().forEach(book -> {
            if (book == null) {
                throw new BookValidationException("Book in create request is empty");
            }
            if (book.getId() != null) {
                throw new BookValidationException("Book id in create request is not empty");
            }
            validateBookRequest(book);
        });
    }

    public void validateUpdateRequest(UserBookRequest request) {
        validateRequest(request);
        if (request.getUserRequest().getId() == null) {
            throw new UserValidationException("User id in update request is empty");
        }
        if (request.getUserRequest().getId() < 0) {
            throw new UserValidationException("User id in update request is negative");
        }
        validateUserRequest(request.getUserRequest());
        request.getBookRequests().forEach(book -> {
            if (book == null) {
                throw new BookValidationException("Book in update request is empty");
            }
            if (book.getId() == null) {
                validateBookRequest(book);
            } else {
                if (book.getId() < 0) {
                    throw new BookValidationException("Book id in update request is negative");
                }
            }
        });
    }

    public void validateId(Long id) {
        if (id < 0) {
            throw new ValidationException("Id in request is negative");
        }
    }

    private void validateRequest(UserBookRequest request) {
        if (request == null) {
            throw new ValidationException("Request body in request is empty");
        }
        if (request.getUserRequest() == null) {
            throw new UserValidationException("User in request is empty");
        }

        if (request.getBookRequests() == null) {
            throw new BookValidationException("Books in request is empty");
        }
    }

    private void validateUserRequest(UserRequest request) {
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new UserValidationException("User full name in request is empty");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new UserValidationException("User title in request is empty");
        }
        if (request.getAge() < 0) {
            throw new UserValidationException("User age in request is negative");
        }
    }

    private void validateBookRequest(BookRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new BookValidationException("Book title in request is empty");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new BookValidationException("Book author in request is empty");
        }
        if (request.getPageCount() < 0) {
            throw new BookValidationException("Book page count in request is negative");
        }
    }
}
