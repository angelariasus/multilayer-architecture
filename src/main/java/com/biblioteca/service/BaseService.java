package com.biblioteca.service;

import java.util.List;

public interface BaseService<T, DTO, ID> {
    DTO save(DTO dto);
    DTO findById(ID id);
    List<DTO> findAll();
    DTO update(DTO dto);
    boolean delete(ID id);
}