package com.itstep.config;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.security.keyvault.secrets.SecretClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureDocIntelligenceConfig {

    @Autowired
    private SecretClient secretClient;

    @Bean
    public DocumentIntelligenceClient documentIntelligenceClient() {
        String key = secretClient.getSecret("DOC-INTELLIGENCE-KEY").getValue();
        String endpoint = secretClient.getSecret("DOC-INTELLIGENCE-ENDPOINT").getValue();

        return new DocumentIntelligenceClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }
}