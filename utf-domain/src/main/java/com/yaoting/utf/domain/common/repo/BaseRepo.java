package com.yaoting.utf.domain.common.repo;

import java.util.List;

public interface BaseRepo<T> {

    T load(Long id);

    void persist(T t);

    List<T> listAll();

    void delete(T entity);
}
