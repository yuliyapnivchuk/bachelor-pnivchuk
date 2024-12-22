package com.itstep.config;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureDocIntelligenceConfig {
    @Value("${azure.doc.intel.endpoint}")
    private String endpoint;

    @Bean
    public DocumentIntelligenceClient documentIntelligenceClient() {
        return new DocumentIntelligenceClientBuilder()
                .credential(new AzureKeyCredential(System.getProperty("AZURE_KEY")))
                .endpoint(endpoint)
                .buildClient();
    }
}