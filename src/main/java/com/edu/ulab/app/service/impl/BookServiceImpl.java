package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        BookEntity book = bookMapper.bookDtoToBookEntity(bookDto);
        log.info("Mapped book: {}", book);
        BookEntity savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookEntityToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookEntity findBook = bookRepository.findByIdForUpdate(Math.toIntExact(bookDto.getId())).orElseThrow(() -> new BookNotFoundException(bookDto.getId()));
        log.info("Find book: {}", findBook);
        BookEntity book = bookMapper.bookDtoToBookEntity(bookDto);
        log.info("Mapped book: {}", book);
        BookEntity savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookEntityToBookDto(savedBook);
    }

    @Override
    public BookDto getBookById(Long id) {
        BookEntity findBook = bookRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new BookNotFoundException(id));
        log.info("Find book: {}", findBook);
        return bookMapper.bookEntityToBookDto(findBook);
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        List<BookEntity> books = bookRepository.findAllByUserId(Math.toIntExact(userId));
        log.info("Book find by user id: {}, count: {}", userId, books.size());
        return bookMapper.bookEntitiesToBookDtos(books);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(Math.toIntExact(id));
        log.info("Book delete by id: {}", id);
    }

    @Override
    public void deleteBooksByUserId(Long userId) {
        try{
            bookRepository.deleteAllByUserId(Math.toIntExact(userId));
            log.info("Book delete by id: {}", userId);
        } catch (EmptyResultDataAccessException e) {
            log.info("No books for current user: {}", userId);
        }
    }
}
