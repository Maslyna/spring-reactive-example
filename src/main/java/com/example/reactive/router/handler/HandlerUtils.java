package com.example.reactive.router.handler;

import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.utils.CollectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HandlerUtils {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 5;
    private static final Direction DEFAULT_DIRECTION = Direction.DESC;

    public static PageRequest extractPageRequest(final ServerRequest request) {
        int page = request.queryParam("page")
                .map(Integer::parseInt)
                .filter(p -> p >= 0)
                .orElse(DEFAULT_PAGE);

        int size = request.queryParam("size")
                .map(Integer::parseInt)
                .filter(s -> s >= 0)
                .orElse(DEFAULT_SIZE);

        List<String> sortBy = request.queryParams().get("sortBy");
        Direction direction = request.queryParam("direction")
                .map(Direction::fromString)
                .orElse(DEFAULT_DIRECTION);

        if (CollectionUtils.hasContent(sortBy)) {
            return PageRequest.of(page, size, direction, sortBy.toArray(new String[sortBy.size()]));
        } else {
            return PageRequest.of(page, size);
        }
    }

    public static Mono<ServerResponse> createResponse(HttpStatusCode status, Object body) {
        return ServerResponse.status(status).contentType(APPLICATION_JSON)
                .bodyValue(body);
    }

    public static Mono<ServerResponse> createResponse(Object body) {
        return createResponse(HttpStatus.OK, body);
    }

    public static Mono<ServerResponse> createResponse(GlobalServiceException e) {
        return createResponse(e.getStatusCode(), e.getBody());
    }

    public static Mono<ServerResponse> createResponse(HttpStatusCode status, Throwable throwable) {
        return createResponse(new GlobalServiceException(status, throwable.getMessage(), throwable));
    }

}
