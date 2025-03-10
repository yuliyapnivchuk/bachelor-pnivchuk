package com.itstep.service;

public interface ImageService {
    void save(Integer expenseId, String name, byte[] image);

    void delete(Integer expenseId);
}
