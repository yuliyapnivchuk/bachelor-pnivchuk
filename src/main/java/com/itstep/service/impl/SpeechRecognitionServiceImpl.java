package com.itstep.service.impl;

import com.itstep.exception.SpeechCouldNotBeRecognized;
import com.itstep.service.SpeechRecognitionService;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static com.itstep.exception.ConstantsUtility.SPEECH_COULD_NOT_BE_RECOGNIZED;
import static com.microsoft.cognitiveservices.speech.AutoDetectSourceLanguageConfig.fromLanguages;
import static com.microsoft.cognitiveservices.speech.ResultReason.Canceled;
import static com.microsoft.cognitiveservices.speech.ResultReason.RecognizedSpeech;
import static com.microsoft.cognitiveservices.speech.CancellationReason.Error;

@Service("Azure")
public class SpeechRecognitionServiceImpl implements SpeechRecognitionService {

    @Autowired
    private SpeechConfig speechConfig;

    @Value("${azure.speech.supported.languages}")
    private String languages;

    @SneakyThrows
    public String convertSpeechToText(String audioFilePath) {

        AutoDetectSourceLanguageConfig autoDetectSourceLanguageConfig = fromLanguages(getSupportedLanguages());

        AudioConfig audioConfig = AudioConfig.fromWavFileInput(audioFilePath);
        SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, autoDetectSourceLanguageConfig, audioConfig);

        Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
        SpeechRecognitionResult speechRecognitionResult = task.get();

        if (speechRecognitionResult.getReason() == RecognizedSpeech) {
            return speechRecognitionResult.getText();
        } else if (speechRecognitionResult.getReason() == Canceled) {
            CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
            String errorMsg = SPEECH_COULD_NOT_BE_RECOGNIZED + cancellation.getReason();

            if (cancellation.getReason() == Error) {
                errorMsg = errorMsg + cancellation.getErrorCode() + cancellation.getErrorDetails();
            }

            throw new SpeechCouldNotBeRecognized(errorMsg);
        } else {
            throw new SpeechCouldNotBeRecognized(SPEECH_COULD_NOT_BE_RECOGNIZED);
        }
    }

    private List<String> getSupportedLanguages() {
        return Arrays.asList(languages.split(","));
    }
}
