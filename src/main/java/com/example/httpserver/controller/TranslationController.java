package com.example.httpserver.controller;

import com.example.httpserver.annotation.MyAutoWired;
import com.example.httpserver.annotation.PostMapping;
import com.example.httpserver.annotation.RequestBody;
import com.example.httpserver.dto.TranslateRequest;
import com.example.httpserver.dto.TranslateResponse;
import com.example.httpserver.service.TranslateService;
import com.example.httpserver.annotation.Controller;

/**
 * TranslationController handles translation requests.
 * It uses the TranslateService to perform translations based on the provided request.
 */
@Controller
public class TranslationController {
    @MyAutoWired
    // 自动注入 TranslateService 实例
    private TranslateService translateService;

    @PostMapping("/translate")
    public TranslateResponse translate(@RequestBody TranslateRequest request) {
        return translateService.translate(request);
    }
}
