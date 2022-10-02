package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.web.request.BookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    BookRequest bookDtoToBookRequest(BookDto bookDto);

    @Mapping(target="person.id", source="userId")
    BookEntity bookDtoToBookEntity(BookDto bookDto);

    @Mapping(target="userId", source="person.id")
    BookDto bookEntityToBookDto(BookEntity entity);

    List<BookDto> bookEntitiesToBookDtos(List<BookEntity> books);
}
