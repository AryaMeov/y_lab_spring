package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.BookEntity;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from BookEntity b where b.id = :id")
    Optional<BookEntity> findByIdForUpdate(long id);

    @Query("select b from BookEntity b where b.person.id = :userId")
    List<BookEntity> findAllByUserId(long userId);

    @Modifying
    @Query("delete from BookEntity b where b.person.id = :userId")
    void deleteAllByUserId(long userId);
}
