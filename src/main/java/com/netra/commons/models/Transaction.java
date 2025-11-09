package com.netra.commons.models;

import com.netra.commons.models.outlet.AccessPoint;
import com.netra.commons.requests.TransactionErrorDTO;
import com.netra.commons.requests.util.TransactionRailDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Transaction extends BaseEntity{

    private String issuerTransactionRef;
    private String systemTransactionRef;//themistra generated
    private FinancialInstitution issuer;
    private FinancialInstitution acquirer;
    private Beneficiary beneficiary;


    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private TransactionErrorDTO error;
    private TransactionType transactionType;
    private TransactionRailDTO transactionRail;
    private String rrn;
    private String stan;
    private Currency currency;
    private Map<String, Object> additionalInformation;
    private String authorizationCode;
    private CardDTO card;//for card based transaction

    private TransactionDisputabilityCheck disputabilityCheck;

}
