package com.example.apigatewayservice.filer;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    // Список открытых точек входа к другим микросервисам
    private static final List<String> openApiEndpoints = List.of(
            "/auth/signin"
    );

    // Список закрытых точек входа к другим микросервисам
    static final Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
