package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = mapper.userDtoToUserEntity(userDto);
        userEntity.setId(repository.getNextId());
        userEntity = repository.save(userEntity);
        return mapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = mapper.userDtoToUserEntity(userDto);
        userEntity = repository.update(userEntity);
        return mapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity userEntity = repository.getById(id);
        return mapper.userEntityToUserDto(userEntity);
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
