package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookDtoRowMapper;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_BOOK = "SELECT * FROM BOOK WHERE ID = ?";
    private static final String SQL_DELETE_BOOK = "DELETE FROM BOOK WHERE ID = ?";
    private static final String SQL_DELETE_BOOKS_BY_USER_ID = "DELETE FROM BOOK WHERE USER_ID = ?";
    private static final String SQL_UPDATE_BOOK = "UPDATE BOOK SET TITLE = ?, AUTHOR = ?, PAGE_COUNT  = ? WHERE ID = ?";
    private static final String SQL_FIND_BOOKS_BY_USER_ID = "SELECT * FROM BOOK WHERE USER_ID = ?";
    private static final String SQL_INSERT_BOOK = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";

    @Override
    public BookDto createBook(BookDto bookDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(SQL_INSERT_BOOK, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Save book: {}", bookDto);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        int rowCount = jdbcTemplate.update(SQL_UPDATE_BOOK, bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPageCount(),
                bookDto.getId());
        if(rowCount == 0) {
            throw new BookNotFoundException(bookDto.getId());
        }
        log.info("Update book: {}", bookDto);
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        BookDto bookDto = jdbcTemplate.queryForObject(SQL_FIND_BOOK, new Object[] { id }, new BookDtoRowMapper());
        if(bookDto == null) {
            throw new BookNotFoundException(id);
        }
        log.info("Find book: {}", bookDto);
        return bookDto;
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        List<BookDto> bookDtos = jdbcTemplate.query(SQL_FIND_BOOKS_BY_USER_ID,new Object[] { userId }, new BookDtoRowMapper());
        log.info("Find book count: {} by userId: {}", bookDtos.size(), userId);
        return bookDtos;
    }

    @Override
    public void deleteBookById(Long id) {
        int rowCount =  jdbcTemplate.update(SQL_DELETE_BOOK, id);
        if(rowCount == 0) {
            throw new BookNotFoundException(id);
        }
    }

    @Override
    public void deleteBooksByUserId(Long userId) {
        jdbcTemplate.update(SQL_DELETE_BOOKS_BY_USER_ID, userId);
    }
}
