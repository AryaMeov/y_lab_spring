package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.PersonEntity;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        PersonEntity person  = new PersonEntity();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        PersonEntity savedPerson  = new PersonEntity();
        savedPerson.setId(1);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
        assertEquals(11, userDtoResult.getAge());
        assertEquals("test name", userDtoResult.getFullName());
        assertEquals("test title", userDtoResult.getTitle());

        //verify

        verify(userMapper).userDtoToPerson(userDto);
        verify(userRepository).save(person);
        verify(userMapper).personToUserDto(savedPerson);
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setAge(12);
        userDto.setFullName("new test name");
        userDto.setTitle("new test title");

        PersonEntity findPerson  = new PersonEntity();
        findPerson.setId(1);
        findPerson.setFullName("test name");
        findPerson.setAge(11);
        findPerson.setTitle("test title");

        PersonEntity personForUpdate  = new PersonEntity();
        personForUpdate.setId(1);
        personForUpdate.setAge(12);
        personForUpdate.setFullName("new test name");
        personForUpdate.setTitle("new test title");

        PersonEntity updatedPerson  = new PersonEntity();
        updatedPerson.setId(1);
        updatedPerson.setAge(12);
        updatedPerson.setFullName("new test name");
        updatedPerson.setTitle("new test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(12);
        result.setFullName("new test name");
        result.setTitle("new test title");


        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(personForUpdate);
        when(userRepository.findByIdForUpdate(personForUpdate.getId())).thenReturn(Optional.of(findPerson));
        when(userRepository.save(personForUpdate)).thenReturn(updatedPerson);
        when(userMapper.personToUserDto(updatedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.updateUser(userDto);
        assertEquals(1L, userDtoResult.getId());
        assertEquals(12, userDtoResult.getAge());
        assertEquals("new test name", userDtoResult.getFullName());
        assertEquals("new test title", userDtoResult.getTitle());

        //verify

        verify(userMapper).userDtoToPerson(userDto);
        verify(userRepository).findByIdForUpdate(personForUpdate.getId());
        verify(userRepository).save(personForUpdate);
        verify(userMapper).personToUserDto(updatedPerson);
    }

    @Test
    @DisplayName("Обновление пользователя. Ошибка: пользователь не найден.")
    void updatePersonNotFound_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setAge(12);
        userDto.setFullName("new test name");
        userDto.setTitle("new test title");


        //when

        when(userRepository.findByIdForUpdate(Math.toIntExact(userDto.getId()))).thenReturn(Optional.empty());


        //then

        assertThatThrownBy(() -> userService.updateUser(userDto))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessage("Person not found by id: "+userDto.getId());

        //verify

        verify(userRepository).findByIdForUpdate(Math.toIntExact(userDto.getId()));
    }

    @Test
    @DisplayName("Получение пользователя. Должно пройти успешно.")
    void getPerson_Test() {
        //given

        long id = 1L;

        PersonEntity person  = new PersonEntity();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userRepository.findById(Math.toIntExact(id))).thenReturn(Optional.of(person));
        when(userMapper.personToUserDto(person)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.getUserById(id);
        assertEquals(1L, userDtoResult.getId());
        assertEquals(11, userDtoResult.getAge());
        assertEquals("test name", userDtoResult.getFullName());
        assertEquals("test title", userDtoResult.getTitle());

        //verify

        verify(userRepository).findById(Math.toIntExact(id));
        verify(userMapper).personToUserDto(person);
    }

    @Test
    @DisplayName("Получение пользователя. Ошибка: пользователь не найден.")
    void getPersonNotFound_Test() {
        //given

        long id = 1L;


        //when

        when(userRepository.findById(Math.toIntExact(id))).thenReturn(Optional.empty());


        //then

        assertThatThrownBy(() -> userService.getUserById(id))
                       .isInstanceOf(PersonNotFoundException.class)
                        .hasMessage("Person not found by id: "+id);
        //verify

        verify(userRepository).findById(Math.toIntExact(id));
    }

    @Test
    @DisplayName("Удаление пользователя. Должно пройти успешно.")
    void deletePerson_Test() {
        //given

        long id = 1L;

        PersonEntity person  = new PersonEntity();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");


        //when

        when(userRepository.findByIdForUpdate(Math.toIntExact(id))).thenReturn(Optional.of(person));


        //then

        userService.deleteUserById(id);

        //verify

        verify(userRepository).findByIdForUpdate(Math.toIntExact(id));
        verify(userRepository).deleteById(Math.toIntExact(id));
    }

    @Test
    @DisplayName("Удаление пользователя. Ошибка: пользователь не найден.")
    void deletePersonNotFound_Test() {
        //given

        long id = 1L;

        //when

        when(userRepository.findByIdForUpdate(Math.toIntExact(id))).thenReturn(Optional.empty());


        //then

        assertThatThrownBy(() -> userService.deleteUserById(id))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessage("Person not found by id: "+id);

        //verify

        verify(userRepository).findByIdForUpdate(Math.toIntExact(id));
    }
}
