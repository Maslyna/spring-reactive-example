package com.example.reactive.router.handler;

import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.utils.CollectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HandlerUtils {

    public static PageRequest extractPageRequest(final ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseUnsignedInt).orElse(0);
        int size = request.queryParam("size").map(Integer::parseUnsignedInt).orElse(5);

        List<String> sortBy = request.queryParams().get("sortBy");
        Direction direction = request.queryParam("direction")
                .map(Direction::fromString)
                .orElse(Direction.DESC);

        if (CollectionUtils.hasContent(sortBy)) {
            return PageRequest.of(page, size, direction, sortBy.toArray(new String[sortBy.size()]));
        } else {
            return PageRequest.of(page, size);
        }
    }

    public static Mono<ServerResponse> createResponse(HttpStatusCode status, Object bodyValue) {
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyValue);
    }

    public static Mono<ServerResponse> createResponse(GlobalServiceException e) {
        return createResponse(e.getStatusCode(), e.getBody());
    }

}
