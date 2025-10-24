package com.netra.commons.enums;

public enum TransactionAction {
    WITHDRAWAL,          // cash out by customer (ATM, POS, Agency, etc.)
    DEPOSIT,             // cash in (ATM deposit, agency deposit, etc.)
    TRANSFER,            // funds movement between accounts
    PURCHASE,            // goods/services payment
    BILL_PAYMENT,        // utilities, telco, subscriptions, etc.
    CASH_OUT,            // alias for agent-led or wallet cashout
    CASH_IN,             // alias for agent-led or wallet cashin
    REVERSAL,            // automated reversal
    ADJUSTMENT,          // back-office correction
    NA                   // fallback for undetermined types
}

