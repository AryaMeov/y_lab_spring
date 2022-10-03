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
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1, insert - 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        PersonEntity person = new PersonEntity();
        person.setAge(111);
        person.setTitle("test title");
        person.setFullName("Test Test");

        //When
        PersonEntity result = userRepository.save(person);
        userRepository.flush();

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertThat(result.getTitle()).isEqualTo("test title");
        assertThat(result.getFullName()).isEqualTo("Test Test");
        assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить юзера. Число select должно равняться 1, update - 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
        //Given
        PersonEntity savedPerson = userRepository.findById(1001).get();

        PersonEntity person = new PersonEntity();
        person.setId(savedPerson.getId());
        person.setAge(111);
        person.setTitle("new test title");
        person.setFullName("new Test Test");

        //When
        PersonEntity result = userRepository.save(person);
        userRepository.flush();

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertThat(result.getTitle()).isEqualTo("new test title");
        assertThat(result.getFullName()).isEqualTo("new Test Test");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    @DisplayName("Получить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPerson_thenAssertDmlCount() {

        //When
        PersonEntity result = userRepository.findById(1001).get();

        //Then
        assertThat(result.getAge()).isEqualTo(55);
        assertThat(result.getTitle()).isEqualTo("reader");
        assertThat(result.getFullName()).isEqualTo("default user");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить юзера. Число select должно равняться 1, delete - 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePerson_thenAssertDmlCount() {
        //When
        userRepository.deleteById(2001);
        userRepository.flush();

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    @DisplayName("Удалить юзера, ошибка: юзера таким id нет. Число select должно равняться 1, delete - 0")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePersonError_thenAssertDmlCount() {
        //When
        assertThatThrownBy(() -> userRepository.deleteById(1111))
                .isInstanceOf(EmptyResultDataAccessException.class);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить юзера, ошибка: на юзера есть ссылка из книг. Число select должно равняться 1, delete - 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePersonErrorWithRefBooks_thenAssertDmlCount() {
        //When
        assertThatThrownBy(() -> {
            userRepository.deleteById(1001);
            userRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }
}
