package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDtoRowMapper implements RowMapper<BookDto> {

    @Override
    public BookDto mapRow(ResultSet resultSet, int i) throws SQLException {
        BookDto bookDto = new BookDto();
        bookDto.setId(resultSet.getLong("id"));
        bookDto.setAuthor(resultSet.getString("author"));
        bookDto.setTitle(resultSet.getString("title"));
        bookDto.setPageCount(resultSet.getLong("page_count"));
        bookDto.setUserId(resultSet.getLong("user_id"));
        return bookDto;
    }
}
