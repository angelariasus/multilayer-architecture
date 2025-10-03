package com.biblioteca.dao;

import java.util.List;

public interface BaseDAO<T, ID> {
    T save(T entity);
    T findById(ID id);
    List<T> findAll();
    T update(T entity);
    boolean delete(ID id);
}