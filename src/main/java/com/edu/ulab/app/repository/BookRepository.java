
package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class BookRepository {
    private final Storage<BookEntity> storage;
    private final AtomicLong atomicLong = new AtomicLong(1);

    public Long getNextId() {
        return atomicLong.getAndIncrement();
    }

    public BookEntity save(BookEntity entity) {
        return storage.save(entity);
    }
    public BookEntity update(BookEntity entity) {
        return Optional.ofNullable(storage.update(entity)).orElseThrow(() -> new BookNotFoundException(entity.getId()));
    }

    public BookEntity getById(Long id) {
        return Optional.ofNullable(storage.getById(id)).orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<BookEntity> getByUserId(Long userId) {
        return storage.getAll().stream().filter(book -> book.getUserId().equals(userId)).toList();
    }

    public void deleteById(Long id) {
        storage.deleteById(id);
    }
}
