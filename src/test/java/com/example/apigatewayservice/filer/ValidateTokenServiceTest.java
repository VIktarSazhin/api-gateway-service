package com.example.apigatewayservice.filer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValidateTokenServiceTest {

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    void postEmptyBodyShouldReturn200OK() {
        String customerId = "123";
        ResponseEntity<Map> responseEntity = testRestTemplate
                .postForEntity("https://httpbin.org/anything/" + customerId, null, Map.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
    }
}