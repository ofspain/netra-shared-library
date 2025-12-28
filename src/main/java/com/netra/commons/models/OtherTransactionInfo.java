package com.netra.commons.models;

import com.netra.commons.requests.TransactionErrorDTO;
import com.netra.commons.requests.util.TransactionRailDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class OtherTransactionInfo extends BaseEntity{

    private TransactionErrorDTO error;
    private TransactionRailDTO transactionRail;
    private String rrn;
    private String stan;
    private Currency currency;
    private String authorizationCode;
    private CardDTO card;//for card based transaction


    private LocalDateTime setelementDate;//if available
    private Boolean isSettled;//if available

    private LocalDateTime postingDate;//if available
    private Boolean isPosted;//if available

    private LocalDateTime reversalDate;//if available
    private Boolean isReversed;//if available

}
