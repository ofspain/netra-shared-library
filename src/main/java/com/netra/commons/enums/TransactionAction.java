package com.netra.commons.enums;

public enum TransactionAction {
    WITHDRAWAL,
    DEPOSIT,
    TRANSFER,
    PURCHASE,

    AIRTIME_PURCHASE,
    DATA_PURCHASE,
    BILL_PAYMENT, //eg dstv subscription, electricity bill payment

    STATUTORY_PAYMENT, //eg government required payment(eg cac registration, saction bills etc), school fees payment

    SERVICE_PAYMENT, //eg basically payment for online services eg a google service, propertypro subscription
}

