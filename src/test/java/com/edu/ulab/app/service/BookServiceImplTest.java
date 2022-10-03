package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.PersonEntity;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        PersonEntity person  = new PersonEntity();
        person.setId(1);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        BookEntity book = new BookEntity();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        BookEntity savedBook = new BookEntity();
        savedBook.setId(1);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when

        when(bookMapper.bookDtoToBookEntity(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookEntityToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
        assertEquals("test author", bookDtoResult.getAuthor());
        assertEquals("test title", bookDtoResult.getTitle());
        assertEquals(1000, bookDtoResult.getPageCount());
        assertEquals(1L, bookDtoResult.getUserId());

        //verify

        verify(bookMapper).bookDtoToBookEntity(bookDto);
        verify(bookRepository).save(book);
        verify(bookMapper).bookEntityToBookDto(savedBook);
    }

    @Test
    @DisplayName("Обновление книги. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        PersonEntity person  = new PersonEntity();
        person.setId(1);

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setUserId(1L);
        bookDto.setAuthor("new test author");
        bookDto.setTitle("new test title");
        bookDto.setPageCount(1001);

        BookEntity findBook = new BookEntity();
        findBook.setPageCount(1000);
        findBook.setTitle("test title");
        findBook.setAuthor("test author");
        findBook.setPerson(person);

        BookEntity newBook = new BookEntity();
        newBook.setId(1);
        newBook.setPageCount(1001);
        newBook.setTitle("new test title");
        newBook.setAuthor("new test author");
        newBook.setPerson(person);

        BookEntity savedBook = new BookEntity();
        savedBook.setId(1);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("new test author");
        result.setTitle("new test title");
        result.setPageCount(1001);

        //when

        when(bookMapper.bookDtoToBookEntity(bookDto)).thenReturn(newBook);
        when(bookRepository.findByIdForUpdate(Math.toIntExact(bookDto.getId()))).thenReturn(Optional.of(findBook));
        when(bookRepository.save(newBook)).thenReturn(savedBook);
        when(bookMapper.bookEntityToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.updateBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
        assertEquals("new test author", bookDtoResult.getAuthor());
        assertEquals("new test title", bookDtoResult.getTitle());
        assertEquals(1001, bookDtoResult.getPageCount());
        assertEquals(1L, bookDtoResult.getUserId());

        //verify

        verify(bookMapper).bookDtoToBookEntity(bookDto);
        verify(bookRepository).findByIdForUpdate(Math.toIntExact(bookDto.getId()));
        verify(bookRepository).save(newBook);
        verify(bookMapper).bookEntityToBookDto(savedBook);
    }

    @Test
    @DisplayName("Обновление книги. Ошибка: книга не найден.")
    void updateBookNotFound_Test() {
        //given

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setUserId(1L);
        bookDto.setAuthor("new test author");
        bookDto.setTitle("new test title");
        bookDto.setPageCount(1001);

        //when

        when(bookRepository.findByIdForUpdate(Math.toIntExact(bookDto.getId()))).thenReturn(Optional.empty());


        //then

        assertThatThrownBy(() -> bookService.updateBook(bookDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found by id: "+bookDto.getId());

        //verify

        verify(bookRepository).findByIdForUpdate(Math.toIntExact(bookDto.getId()));
    }

    @Test
    @DisplayName("Получение книги. Должно пройти успешно.")
    void getBook_Test() {
        //given
        long id = 1L;

        PersonEntity person  = new PersonEntity();
        person.setId(1);

        BookEntity findBook = new BookEntity();
        findBook.setId(1);
        findBook.setPageCount(1000);
        findBook.setTitle("test title");
        findBook.setAuthor("test author");
        findBook.setPerson(person);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        //when

        when(bookRepository.findById(Math.toIntExact(id))).thenReturn(Optional.of(findBook));
        when(bookMapper.bookEntityToBookDto(findBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.getBookById(id);
        assertEquals(1L, bookDtoResult.getId());
        assertEquals("test author", bookDtoResult.getAuthor());
        assertEquals("test title", bookDtoResult.getTitle());
        assertEquals(1000, bookDtoResult.getPageCount());
        assertEquals(1L, bookDtoResult.getUserId());

        //verify

        verify(bookRepository).findById(Math.toIntExact(id));
        verify(bookMapper).bookEntityToBookDto(findBook);
    }

    @Test
    @DisplayName("Получение книги. Ошибка: книга не найден.")
    void getBookNotFound_Test() {
        //given
        long id = 1L;

        //when

        when(bookRepository.findById(Math.toIntExact(id))).thenReturn(Optional.empty());


        //then
        assertThatThrownBy(() -> bookService.getBookById(id))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found by id: "+id);

        //verify

        verify(bookRepository).findById(Math.toIntExact(id));
    }

    @Test
    @DisplayName("Получение всех книг пользователя. Должно пройти успешно.")
    void getBooksByUserId_Test() {
        //given
        long userId = 1L;

        PersonEntity person  = new PersonEntity();
        person.setId(1);

        BookEntity firstBook = new BookEntity();
        firstBook.setId(1);
        firstBook.setPageCount(1000);
        firstBook.setTitle("test title");
        firstBook.setAuthor("test author");
        firstBook.setPerson(person);

        BookEntity secondBook = new BookEntity();
        secondBook.setId(2);
        secondBook.setPageCount(2000);
        secondBook.setTitle("second test title");
        secondBook.setAuthor("second test author");
        secondBook.setPerson(person);

        List<BookEntity> books = List.of(firstBook, secondBook);

        BookDto firstBookDto = new BookDto();
        firstBookDto.setId(1L);
        firstBookDto.setUserId(1L);
        firstBookDto.setAuthor("test author");
        firstBookDto.setTitle("test title");
        firstBookDto.setPageCount(1000);

        BookDto secondBookDto = new BookDto();
        secondBookDto.setId(2L);
        secondBookDto.setUserId(1L);
        secondBookDto.setAuthor("second test author");
        secondBookDto.setTitle("second test title");
        secondBookDto.setPageCount(2000);

        List<BookDto> result = List.of(firstBookDto, secondBookDto);

        //when

        when(bookRepository.findAllByUserId(Math.toIntExact(userId))).thenReturn(books);
        when(bookMapper.bookEntitiesToBookDtos(books)).thenReturn(result);


        //then
        List<BookDto> bookDtoResult = bookService.getBooksByUserId(userId);
        assertEquals(2L, bookDtoResult.size());
        assertEquals(1L, bookDtoResult.get(0).getId());
        assertEquals("test author", bookDtoResult.get(0).getAuthor());
        assertEquals("test title", bookDtoResult.get(0).getTitle());
        assertEquals(1000, bookDtoResult.get(0).getPageCount());
        assertEquals(1L, bookDtoResult.get(0).getUserId());
        assertEquals(2L, bookDtoResult.get(1).getId());
        assertEquals("second test author", bookDtoResult.get(1).getAuthor());
        assertEquals("second test title", bookDtoResult.get(1).getTitle());
        assertEquals(2000, bookDtoResult.get(1).getPageCount());
        assertEquals(1L, bookDtoResult.get(1).getUserId());

        //verify

        verify(bookRepository).findAllByUserId(Math.toIntExact(userId));
        verify(bookMapper).bookEntitiesToBookDtos(books);
    }

    @Test
    @DisplayName("Удаление всех книг пользователя. Должно пройти успешно.")
    void deleteAllBooksByUserId_Test() {
        //given
        long userId = 1L;

        //then
        bookService.deleteBooksByUserId(userId);

        //verify

        verify(bookRepository).deleteAllByUserId(Math.toIntExact(userId));
    }


    @Test
    @DisplayName("Удаление всех книг пользователя. Должно пройти успешно(книг у пользователя нет).")
    void deleteAllBooksByUserIdNotBooks_Test() {
        //given
        long userId = 1L;

        doThrow(EmptyResultDataAccessException.class).when(bookRepository).deleteAllByUserId(Math.toIntExact(userId));

        //then
        bookService.deleteBooksByUserId(userId);

        //verify

        verify(bookRepository).deleteAllByUserId(Math.toIntExact(userId));
    }
}
