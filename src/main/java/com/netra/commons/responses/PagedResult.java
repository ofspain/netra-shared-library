package com.netra.commons.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class PagedResult<T> {
    private final List<T> data;
    private final Metadata meta;

    @Data
    @AllArgsConstructor
    public static class Metadata {
        private long total;
        private int limit;
        private int offset;
        private boolean hasMore;
    }
}