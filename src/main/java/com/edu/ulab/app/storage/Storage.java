package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BaseEntity;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

// создать хранилище в котором будут содержаться данные
// сделать абстракции через которые можно будет производить операции с хранилищем
// продумать логику поиска и сохранения
// продумать возможные ошибки
// учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
// продумать что у узера может быть много книг и нужно создать эту связь
// так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции
public class Storage<T extends BaseEntity> {
    private final ConcurrentHashMap<Long, T> storage = new ConcurrentHashMap<>();

    public T save(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    public T update(T entity) {
        return storage.computeIfPresent(entity.getId(), (key, value) -> entity);
    }

    public T getById(Long id) {
        return storage.get(id);
    }

    public Collection<T> getAll() {
        return storage.values();
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

}
