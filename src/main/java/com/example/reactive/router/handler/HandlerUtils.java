package com.example.reactive.router.handler;

import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.utils.CollectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        int page = extractPageNumber(request);
        int size = extractPageSize(request);
        List<String> sortBy = extractSortBy(request);
        Direction direction = extractDirection(request);

        if (CollectionUtils.hasContent(sortBy)) {
            return PageRequest.of(page, size, direction, sortBy.toArray(new String[sortBy.size()]));
        } else {
            return PageRequest.of(page, size);
        }
    }

    public static List<String> extractSortBy(final ServerRequest request) {
        return request.queryParams().get("sortBy");
    }

    public static Direction extractDirection(final ServerRequest request) {
        try {
            return request.queryParam("order")
                    .map(Direction::fromString)
                    .orElse(DEFAULT_DIRECTION);
        } catch (IllegalArgumentException ex) {
            return DEFAULT_DIRECTION;
        }
    }

    public static int extractPageNumber(final ServerRequest request) {
        try {
            return request.queryParam("page")
                    .map(Integer::parseInt)
                    .filter(p -> p >= 0)
                    .orElse(DEFAULT_PAGE);
        } catch (NumberFormatException ex) {
            return DEFAULT_PAGE;
        }
    }

    public static int extractPageSize(final ServerRequest request) {
        try {
            return request.queryParam("size")
                    .map(Integer::parseInt)
                    .filter(s -> s >= 0)
                    .orElse(DEFAULT_SIZE);
        } catch (NumberFormatException ex) {
            return DEFAULT_PAGE;
        }
    }

    public static Mono<ServerResponse> createResponse(final HttpStatusCode status) {
        return ServerResponse.status(status).build();
    }

    public static Mono<ServerResponse> createResponse(final HttpStatusCode status, final Object body) {
        return ServerResponse.status(status).contentType(APPLICATION_JSON)
                .bodyValue(body);
    }

    public static Mono<ServerResponse> createResponse(final Object body) {
        return createResponse(HttpStatus.OK, body);
    }

    public static Mono<ServerResponse> createResponse(final GlobalServiceException e) {
        return createResponse(e.getStatusCode(), e.getBody());
    }

    public static Mono<ServerResponse> createResponse(final HttpStatusCode status, final Throwable throwable) {
        return createResponse(new GlobalServiceException(status, throwable.getMessage(), throwable));
    }
}
