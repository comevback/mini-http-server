package com.example.httpserver.dto;

import lombok.Data;

@Data
public class TranslateResponse {
    private String translatedText;
    private String sourceLanguage;
    private String targetLanguage;

    public TranslateResponse() {
    }

    public TranslateResponse(String translatedText, String sourceLanguage, String targetLanguage) {
        this.translatedText = translatedText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }
}
