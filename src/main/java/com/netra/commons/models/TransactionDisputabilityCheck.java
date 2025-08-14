package com.netra.commons.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDisputabilityCheck {

    private LocalDateTime setelementDate;//if available
    private Boolean isSettled;//if available

    private LocalDateTime postingDate;//if available
    private Boolean isPosted;//if available

    private LocalDateTime reversalDate;//if available
    private Boolean isReversed;//if available
}
