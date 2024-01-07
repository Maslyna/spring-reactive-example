package com.example.reactive.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

    public static boolean hasContent(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
