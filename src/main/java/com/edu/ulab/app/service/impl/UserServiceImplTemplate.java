package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.mapper.UserDtoRowMapper;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplTemplate implements UserService {
    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_PERSON = "SELECT * FROM PERSON WHERE ID = ?";
    private static final String SQL_DELETE_PERSON = "DELETE FROM PERSON WHERE ID = ?";
    private static final String SQL_UPDATE_PERSON = "UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE  = ? WHERE ID = ?";
    private static final String SQL_INSERT_PERSON = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";

    @Override
    public UserDto createUser(UserDto userDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_PERSON, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        int rowCount = jdbcTemplate.update(SQL_UPDATE_PERSON, userDto.getFullName(), userDto.getTitle(), userDto.getAge(),
                userDto.getId());
        if(rowCount == 0) {
            throw new PersonNotFoundException(userDto.getId());
        }
        log.info("Update user: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        try {
            UserDto userDto = jdbcTemplate.queryForObject(SQL_FIND_PERSON, new Object[] { id }, new UserDtoRowMapper());
            log.info("Find user: {}", userDto);
            return userDto;
        } catch (EmptyResultDataAccessException e) {
            log.info("User not find: {}", id);
            throw new PersonNotFoundException(id);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        int rowCount =  jdbcTemplate.update(SQL_DELETE_PERSON, id);
        if(rowCount == 0) {
            throw new PersonNotFoundException(id);
        }
    }
}
