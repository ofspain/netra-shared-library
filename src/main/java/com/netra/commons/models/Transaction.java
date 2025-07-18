package com.netra.commons.models;

import com.netra.commons.contracts.Auditable;
import com.netra.commons.enums.TransactionChannel;
import com.netra.commons.enums.TransactionErrorType;
import com.netra.commons.enums.TransactionInstrument;
import com.netra.commons.requests.util.TransactionRailDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction extends BaseEntity implements Auditable {

    private String transactionRef;
    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionErrorType errorType;
    private TransactionRailDTO transactionRailDTO;
    private String retrievalReferenceNumber;
    private String stan;
    private String transactionCurrencyCode;
}
