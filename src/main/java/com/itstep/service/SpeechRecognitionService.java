package com.itstep.service;

import com.itstep.exception.SpeechCouldNotBeRecognized;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.itstep.exception.ConstantsUtility.SPEECH_COULD_NOT_BE_RECOGNIZED;

@Service
@AllArgsConstructor
public class SpeechRecognitionService {

    public String convertSpeechToText(String audioFilePath) throws InterruptedException, ExecutionException {

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(
                System.getProperty("SPEECH_KEY"),
                System.getProperty("SPEECH_REGION")
        );

        AutoDetectSourceLanguageConfig autoDetectSourceLanguageConfig =
                AutoDetectSourceLanguageConfig.fromLanguages(Arrays.asList("en-US", "uk-UA"));

        AudioConfig audioConfig = AudioConfig.fromWavFileInput(audioFilePath);
        SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, autoDetectSourceLanguageConfig, audioConfig);

        Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
        SpeechRecognitionResult speechRecognitionResult = task.get();

        if (speechRecognitionResult.getReason() == ResultReason.RecognizedSpeech) {
            return speechRecognitionResult.getText();
        } else if (speechRecognitionResult.getReason() == ResultReason.Canceled) {
            CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
            String errorMsg = SPEECH_COULD_NOT_BE_RECOGNIZED + cancellation.getReason();

            if (cancellation.getReason() == CancellationReason.Error) {
                errorMsg = errorMsg + cancellation.getErrorCode() + cancellation.getErrorDetails();
            }

            throw new SpeechCouldNotBeRecognized(errorMsg);
        } else {
            throw new SpeechCouldNotBeRecognized(SPEECH_COULD_NOT_BE_RECOGNIZED);
        }
    }
}
