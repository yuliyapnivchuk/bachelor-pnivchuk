package com.itstep.config;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobStorageConfig {

    @Autowired
    private SecretClient secretClient;

    @Bean
    public BlobServiceClient blobServiceClient() {
        String key = secretClient.getSecret("BLOB-STORAGE-KEY").getValue();
        String accountName = secretClient.getSecret("BLOB-STORAGE-ACCOUNT-NAME").getValue();

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, key);

        return new BlobServiceClientBuilder()
                .endpoint("https://" + accountName + ".blob.core.windows.net")
                .credential(credential)
                .buildClient();
    }
}
