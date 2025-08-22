package com.netra.commons.requests;

import java.util.Set;

import static com.netra.commons.requests.SortedSearchParams.SortDirection.ASC;

public interface SortedSearchParams {
    default SortDirection getSortDirection(){
        return ASC;
    }

    default String getSortColumn(){
        return "created_at";
    }

    default Set<String> allowedSortColumns(){
        return Set.of("id","created_at", "updated_at");
    }

    public enum SortDirection {
        ASC, DESC
    }
}
