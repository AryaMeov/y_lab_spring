package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookMapper mapper;
    @Override
    public BookDto createBook(BookDto bookDto) {
        BookEntity bookEntity = mapper.bookDtoToBookEntity(bookDto);
        bookEntity.setId(repository.getNextId());
        bookEntity = repository.save(bookEntity);
        return mapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookEntity bookEntity = mapper.bookDtoToBookEntity(bookDto);
        bookEntity = repository.update(bookEntity);
        return mapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto getBookById(Long id) {
        BookEntity bookEntity = repository.getById(id);
        return mapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        return repository.getByUserId(userId).stream().map(mapper::bookEntityToBookDto).toList();
    }

    @Override
    public void deleteBookById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteBooksByUserId(Long userId) {
        repository.getByUserId(userId).stream().map(BookEntity::getId).forEach(this::deleteBookById);
    }
}
