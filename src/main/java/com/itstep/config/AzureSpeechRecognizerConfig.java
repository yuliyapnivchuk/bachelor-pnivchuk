package com.itstep.config;

import com.azure.security.keyvault.secrets.SecretClient;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureSpeechRecognizerConfig {

    @Autowired
    SecretClient secretClient;

    @Bean
    public SpeechConfig speechRecognizer() {
        String key = secretClient.getSecret("SPEECH-RECOGNIZER-KEY").getValue();
        String region = secretClient.getSecret("SPEECH-RECOGNIZER-REGION").getValue();
        return SpeechConfig.fromSubscription(key, region);
    }
}
