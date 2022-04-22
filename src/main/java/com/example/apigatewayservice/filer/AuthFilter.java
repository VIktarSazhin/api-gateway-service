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

    @Autowired
    private RestService restService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        if (RouterValidator.isSecured.test(request)) {

            if (this.isAuthMissing(request))
                return this.onError(exchange, HttpStatus.NON_AUTHORITATIVE_INFORMATION);

            String authHeader;
            String[] parts = null;

            try {
                authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                parts = authHeader.split(" ");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (parts != null && (parts.length != 2 || !"Bearer".equals(parts[0]))) {
                return this.onError(exchange, HttpStatus.BAD_REQUEST);
            }

            if (parts != null && restService.createPost(parts[1]).equals("false")) {
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
    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
