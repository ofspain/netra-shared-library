package com.netra.commons.requests;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Data
public class DatedSearchParams extends PagingSearchParams<DatedSearchParams> {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
