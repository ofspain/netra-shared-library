package com.netra.commons.requests;

import lombok.ToString;

@ToString
public class PagingSearchParams{

    private static final int PAGE_SIZE_MAX = 100;

    private Integer pageNum;
    private Integer pageSize;

    public PagingSearchParams() {
        setPageNum(1);
        setPageSize(PAGE_SIZE_MAX);
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum == null || pageNum < 1) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1 || pageSize > PAGE_SIZE_MAX) {
            this.pageSize = PAGE_SIZE_MAX;
        } else {
            this.pageSize = pageSize;
        }
    }

}

