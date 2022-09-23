package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.validation.UserBookRequestValidationService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final UserBookRequestValidationService validatingService;

    public UserBookResponse createUserWithBooks(UserBookRequest request) {
        log.info("Got user book create request: {}", request);
        validatingService.validateCreateRequest(request);
        log.info("User book update request is valid: {}", request);
        UserDto userDto = userMapper.userRequestToUserDto(request.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = request.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest request) {
        log.info("Got user book update request: {}", request);
        validatingService.validateUpdateRequest(request);
        log.info("User book update request is valid: {}", request);
        UserDto userDto = userMapper.userRequestToUserDto(request.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto updatedUser = userService.updateUser(userDto);
        log.info("Updated user: {}", updatedUser);

        Set<Long> foundBookIdList = bookService.getBooksByUserId(updatedUser.getId())
                .stream()
                .map(BookDto::getId)
                .collect(Collectors.toSet());
        log.info("Found book ids: {}", foundBookIdList);

        List<Long> bookIdList = request.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(updatedUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookDto -> {
                    //создает книгу, если не пришел ид книги
                    if(bookDto.getId() == null) {
                        return bookService.createBook(bookDto);
                    } else {
                        //обновляет книгу, если пришел ид книги
                        foundBookIdList.remove(bookDto.getId());
                        return bookService.updateBook(bookDto);
                    }
                })
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        //удаляет книги, которые не пришли в обновление пользователя
        if(!foundBookIdList.isEmpty()) {
            foundBookIdList.forEach(bookService::deleteBookById);
        }

        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Got user book get request by id: {}", userId);
        validatingService.validateId(userId);
        UserDto foundUser = userService.getUserById(userId);
        log.info("Found user: {}", foundUser);
        List<Long> bookIdList = bookService.getBooksByUserId(foundUser.getId())
                .stream()
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(foundUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        log.info("Got user book delete request by id: {}", userId);
        validatingService.validateId(userId);
        userService.deleteUserById(userId);
        log.info("Delete user: {}", userId);
        bookService.deleteBooksByUserId(userId);
        log.info("Delete books by user: {}", userId);
    }
}
