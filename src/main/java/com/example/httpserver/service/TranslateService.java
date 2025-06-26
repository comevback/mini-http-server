package com.example.httpserver.service;

import com.example.httpserver.dto.TranslateRequest;
import com.example.httpserver.dto.TranslateResponse;
import com.example.httpserver.annotation.Service;

public interface TranslateService {
    /**
     * Translates the given text from source language to target language.
     *
     * @param request The TranslateRequest object containing the text to be translated,
     * @return A TranslateResponse object containing the translated text and language information.
     */
    TranslateResponse translate(TranslateRequest request);
}
