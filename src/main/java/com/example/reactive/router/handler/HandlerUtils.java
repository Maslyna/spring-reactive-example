package com.example.reactive.router.handler;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;

public class HandlerUtils {

    public static PageRequest extractPageRequest(final ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseUnsignedInt).orElse(0);
        int size = request.queryParam("size").map(Integer::parseUnsignedInt).orElse(5);

        List<String> sortBy = request.queryParams().get("sortBy");
        Sort.Direction direction = request.queryParam("direction")
                .map(Sort.Direction::fromString)
                .orElse(Sort.Direction.DESC);

        if (sortBy != null && !sortBy.isEmpty()) {
            return PageRequest.of(page, size, direction, sortBy.toArray(new String[0]));
        } else {
            return PageRequest.of(page, size);
        }
    }
}
