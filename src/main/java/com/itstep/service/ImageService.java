package com.itstep.service;

import java.io.IOException;

public interface ImageService {
    void save(Integer expenseId, String name, byte[] image);
    void delete(Integer expenseId);
    byte[] getImage(Integer expenseId) throws IOException;
}
