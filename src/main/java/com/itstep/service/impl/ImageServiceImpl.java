package com.itstep.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.itstep.entity.Expense;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.repository.ExpenseRepository;
import com.itstep.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.itstep.exception.ConstantsUtility.EXPENSE_WITH_SUCH_ID_NOT_FOUND;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private BlobServiceClient blobServiceClient;
    private ExpenseRepository expenseRepository;

    @Override
    @SneakyThrows
    public void save(Integer expenseId, String name, byte[] image) {
        expenseRepository.findById(expenseId).orElseThrow(() -> new ExpenseNotFound(EXPENSE_WITH_SUCH_ID_NOT_FOUND));

        BlobContainerClient blobContainerClient =
                blobServiceClient.createBlobContainerIfNotExists("exp-" + expenseId);

        blobContainerClient.listBlobs()
                .forEach(blob -> blobContainerClient.getBlobClient(blob.getName()).delete());

        BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(name).getBlockBlobClient();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(image)) {
            blockBlobClient.upload(dataStream, image.length, true);
        }

        expenseRepository.updateImageName(name, expenseId);
    }

    public byte[] getImage(Integer expenseId) throws IOException {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFound(EXPENSE_WITH_SUCH_ID_NOT_FOUND));

        BlobContainerClient blobContainerClient =
                blobServiceClient.getBlobContainerClient("exp-" + expenseId);

        BlobClient blobClient = blobContainerClient.getBlobClient(expense.getImage());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            blobClient.downloadStream(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    @SneakyThrows
    public void delete(Integer expenseId) {
        expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFound(EXPENSE_WITH_SUCH_ID_NOT_FOUND));

        expenseRepository.updateImageName(null, expenseId);

        blobServiceClient.deleteBlobContainerIfExists("exp-" + expenseId);
    }
}
