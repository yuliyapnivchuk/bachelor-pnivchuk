package com.itstep.config;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.AzureCliCredentialBuilder;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyVaultConfig {
    @Value("${azure.key.vault.endpoint}")
    private String url;

    @Bean
    public SecretClient keyVaultClient() {
        TokenCredential credential = new AzureCliCredentialBuilder().build();

//        ManagedIdentityCredential credential = new ManagedIdentityCredentialBuilder().build();

        return new SecretClientBuilder()
                .vaultUrl(url)
                .credential(credential)
                .buildClient();
    }
}
