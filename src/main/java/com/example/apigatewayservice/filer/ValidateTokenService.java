package com.example.apigatewayservice.filer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ValidateTokenService {
    private final RestTemplate restTemplate;

    String url = "http://localhost:8090/auth/validateToken?token=";

    @Autowired
    public ValidateTokenService() {
        this.restTemplate = new RestTemplateBuilder().build();
    }

    public String checkToken(String parts) {

        HttpHeaders headers = new HttpHeaders();

        // Добавляем к нашему запросу Bearer Token
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(parts);

        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.postForObject(url, request, String.class);
    }
}
