
package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class UserRepository {
    private final Storage<UserEntity> storage;
    private final AtomicLong atomicLong = new AtomicLong(1);

    public Long getNextId() {
        return atomicLong.getAndIncrement();
    }

    public UserEntity save(UserEntity entity) {
        return storage.save(entity);
    }
    public UserEntity update(UserEntity entity) {
        return Optional.ofNullable(storage.update(entity)).orElseThrow(() -> new UserNotFoundException(entity.getId()));
    }

    public UserEntity getById(Long id) {
        return Optional.ofNullable(storage.getById(id)).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteById(Long id) {
        storage.deleteById(id);
    }
}
