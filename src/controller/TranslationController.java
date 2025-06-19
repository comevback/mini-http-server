package controller;

import annotation.PostMapping;
import annotation.RequestBody;
import dto.TranslateRequest;
import dto.TranslateResponse;
import service.TranslateService;
import service.TranslateServiceImpl;

public class TranslationController {
    @PostMapping("/translate")
    public TranslateResponse translate(@RequestBody TranslateRequest request) {
        TranslateService translateService = new TranslateServiceImpl();
        return translateService.translate(request);
    }
}
