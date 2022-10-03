package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.PersonEntity;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        PersonEntity user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        PersonEntity savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        PersonEntity findUser = userRepository.findByIdForUpdate(Math.toIntExact(userDto.getId())).orElseThrow(() -> new PersonNotFoundException(userDto.getId()));
        log.info("Find user: {}", findUser);
        PersonEntity user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        PersonEntity savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        PersonEntity user = userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new PersonNotFoundException(id));
        log.info("Found user: {}", user);
        return userMapper.personToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        PersonEntity findUser = userRepository.findByIdForUpdate(Math.toIntExact(id)).orElseThrow(() -> new PersonNotFoundException(id));
        log.info("Find user: {}", findUser);
        userRepository.deleteById(Math.toIntExact(id));
        log.info("User deleted");
    }
}
