package com.example.httpserver.dto;

import lombok.Data;

@Data
public class TranslateRequest {
    private String text;
    private String sourceLanguage;
    private String targetLanguage;

    public TranslateRequest() {
    }

    public TranslateRequest(String text, String sourceLanguage, String targetLanguage) {
        this.text = text;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }
}
