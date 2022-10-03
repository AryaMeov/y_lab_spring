package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.PersonEntity;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    private PersonEntity person;

    @BeforeEach
    void setUp() {
        person = userRepository.findById(1001).get();
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу. Число select должно равняться 1, insert - 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertBook_thenAssertDmlCount() {
        //Given

        BookEntity book = new BookEntity();
        book.setAuthor("Test Author");
        book.setTitle("test title");
        book.setPageCount(1000);
        book.setPerson(person);

        //When
        BookEntity result = bookRepository.save(book);
        bookRepository.flush();

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test title");
        assertThat(result.getAuthor()).isEqualTo("Test Author");
        assertThat(result.getPerson().getId()).isEqualTo(1001);
        assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Сохранить книгу. ошибка: пустой пользователь. Число select должно равняться 1, insert - 0")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertBookError_thenAssertDmlCount() {
        //Given

        BookEntity book = new BookEntity();
        book.setAuthor("Test Author");
        book.setTitle("test title");
        book.setPageCount(1000);

        //When
        assertThatThrownBy(() -> {
            bookRepository.save(book);
            bookRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу. Число select должно равняться 1, update - 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
        BookEntity savedBook = bookRepository.findById(2002).get();

        BookEntity newBook = new BookEntity();
        newBook.setId(savedBook.getId());
        newBook.setAuthor("New Test Author");
        newBook.setTitle("new test");
        newBook.setPageCount(2000);
        newBook.setPerson(person);

        //When
        BookEntity result = bookRepository.save(newBook);
        bookRepository.flush();

        //Then
        assertThat(result.getPageCount()).isEqualTo(2000);
        assertThat(result.getAuthor()).isEqualTo("New Test Author");
        assertThat(result.getTitle()).isEqualTo("new test");
        assertThat(result.getId()).isEqualTo(savedBook.getId());
        assertThat(result.getPerson().getId()).isEqualTo(1001);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    @DisplayName("Получить книгу. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBook_thenAssertDmlCount() {

        //When
        BookEntity result = bookRepository.findById(2002).get();

        //Then
        assertThat(result.getPageCount()).isEqualTo(5500);
        assertThat(result.getTitle()).isEqualTo("default book");
        assertThat(result.getAuthor()).isEqualTo("author");
        assertThat(result.getPerson().getId()).isEqualTo(1001);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить книгу. Число select должно равняться 1, delete - 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBook_thenAssertDmlCount() {
        //When
        bookRepository.deleteById(2002);
        bookRepository.flush();

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    @DisplayName("Удалить книгу, ошибка: книги с таким id нет. Число select должно равняться 1, delete - 0")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBookError_thenAssertDmlCount() {
        //When
        assertThatThrownBy(() -> bookRepository.deleteById(1111))
                .isInstanceOf(EmptyResultDataAccessException.class);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить все книги автора. Число delete должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteAllBooksByUserId_thenAssertDmlCount() {
        //When
        bookRepository.deleteAllByUserId(1001);
        bookRepository.flush();

        //Then
        assertSelectCount(0);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }
}
