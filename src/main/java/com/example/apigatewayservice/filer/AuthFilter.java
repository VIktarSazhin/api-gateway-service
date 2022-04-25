package com.example.apigatewayservice.filer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GatewayFilter {

    private final ValidateTokenService validateTokenService;

    @Autowired
    public AuthFilter(ValidateTokenService validateTokenService) {
        this.validateTokenService = validateTokenService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // Проверка находится ли запрос в списке закрытых точек входа
        if (RouterValidator.isSecured.test(request)) {

            // Проверка есть ли у запроса авторизированный статус
            if (!request.getHeaders().containsKey("Authorization"))
                return this.onError(exchange, HttpStatus.NON_AUTHORITATIVE_INFORMATION);

            String authHeader;
            String[] parts = null;

            try {
                authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                parts = authHeader.split(" ");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            // Проверка есть ли у запроса Bearer token
            if (parts != null && (parts.length != 2 || !"Bearer".equals(parts[0]))) {
                return this.onError(exchange, HttpStatus.BAD_REQUEST);
            }

            // Проверка на валидность токена в запросе
            if (parts != null && validateTokenService.checkToken(parts[1]).equals("false")) {
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
