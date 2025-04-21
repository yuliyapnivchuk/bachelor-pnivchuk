package com.itstep.service;

import com.azure.storage.blob.*;
import com.itstep.entity.Expense;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.repository.ExpenseRepository;
import com.itstep.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class ImageServiceTests {

    @Mock
    BlobServiceClient blobServiceClient;

    @Mock
    BlobContainerClient blobContainerClient;

    @Spy
    BlobClient blobClient = new BlobClientBuilder()
            .blobName("test.jpg")
            .endpoint("https://accountName.blob.core.windows.net")
            .buildClient();

    @Mock
    ExpenseRepository expenseRepository;

    @InjectMocks
    ImageServiceImpl imageService;

    @Test
    void saveImageExpenseNotFoundTest() {
        assertThrows(ExpenseNotFound.class, () -> imageService.save(1, "name", "test-image".getBytes()));
    }

    @Test
    void getImageExpenseNotFoundTest() {
        assertThrows(ExpenseNotFound.class, () -> imageService.getImage(1));
    }

    @Test
    void deleteImageExpenseNotFoundTest() {
        assertThrows(ExpenseNotFound.class, () -> imageService.delete(1));
    }

    @Test
    void deleteImageTest() {
        when(expenseRepository.findById(any())).thenReturn(Optional.of(new Expense()));
        doNothing().when(expenseRepository).updateImageName(any(), any());
        when(blobServiceClient.deleteBlobContainerIfExists(any())).thenReturn(true);

        imageService.delete(1);

        verify(expenseRepository, times(1)).findById(any());
        verify(expenseRepository, times(1)).updateImageName(any(), any());
        verify(blobServiceClient, times(1)).deleteBlobContainerIfExists(any());
    }
}
