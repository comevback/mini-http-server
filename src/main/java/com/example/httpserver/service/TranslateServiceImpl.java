package com.example.httpserver.service;

import com.example.httpserver.annotation.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.httpserver.dto.TranslateRequest;
import com.example.httpserver.dto.TranslateResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import com.example.httpserver.annotation.Service;

/**
 * 翻译服务实现
 * 使用 Google Translate API 进行文本翻译
 */
@Service
public class TranslateServiceImpl implements TranslateService {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Log("翻译服务调用")
    public TranslateResponse translate(TranslateRequest request) {
        try {
            String encodedText = URLEncoder.encode(request.getText(), "UTF-8");
            String urlStr = String.format(
                    "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s",
                    request.getSourceLanguage(), request.getTargetLanguage(), encodedText
            );

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // 解析 JSON 响应
            JsonNode root = mapper.readTree(response.toString());
            String translated = root.get(0).get(0).get(0).asText();
            System.out.println("Translation: " + request.getText() + " -> "+ translated);

            return new TranslateResponse(translated,
                    request.getSourceLanguage(), request.getTargetLanguage());

        } catch (Exception e) {
            e.printStackTrace();
            return new TranslateResponse("Translation failed: " + e.getMessage(),
                    request.getSourceLanguage(), request.getTargetLanguage());
        }
    }
}
