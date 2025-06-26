package com.example.httpserver.controller;

import com.example.httpserver.annotation.*;
import com.example.httpserver.dto.TranslateRequest;
import com.example.httpserver.dto.TranslateResponse;
import com.example.httpserver.service.TranslateService;

/**
 * 翻译控制器
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
