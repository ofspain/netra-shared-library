package com.netra.commons.requests;

import lombok.ToString;

@ToString
public class PagingSearchParams<T extends PagingSearchParams<T>> {

    private static final int PAGE_SIZE_MAX = 100;
    private static final int DEFAULT_PAGE_NUM = 1;

    private Integer pageNum;
    private Integer pageSize;

    public PagingSearchParams() {
        this.pageNum = DEFAULT_PAGE_NUM;
        this.pageSize = PAGE_SIZE_MAX;
    }

    // --- Fluent API ---
    @SuppressWarnings("unchecked")
    public T withPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return (T) this;
    }

    public Integer getPageNum() {
        return safePageNum();
    }

    public Integer getPageSize() {
        return safePageSize();
    }

    public int calculateDBOffset() {
        return (safePageNum() - 1) * safePageSize();
    }

    // --- Private sanitizers ---
    private int safePageNum() {
        return (pageNum == null || pageNum < 1) ? DEFAULT_PAGE_NUM : pageNum;
    }

    private int safePageSize() {
        return (pageSize == null || pageSize < 1 || pageSize > PAGE_SIZE_MAX)
                ? PAGE_SIZE_MAX
                : pageSize;
    }
}
