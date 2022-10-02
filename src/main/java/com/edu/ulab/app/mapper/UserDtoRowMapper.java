package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDtoRowMapper implements RowMapper<UserDto> {
    @Override
    public UserDto mapRow(ResultSet resultSet, int i) throws SQLException {

        UserDto userDto = new UserDto();
        userDto.setId(resultSet.getLong("id"));
        userDto.setFullName(resultSet.getString("full_name"));
        userDto.setTitle(resultSet.getString("title"));
        userDto.setAge(resultSet.getInt("age"));
        return userDto;
    }
}
