package com.netra.commons.enums;

public enum TransactionProcessor {
    NIP,                // Nigeria Inter-Bank Payment
    POS_SWITCH,         // Any POS switching network
    USSD_GATEWAY,       // Telco USSD gateway processors
    WALLET_PROCESSOR,   // Wallet ecosystems (e.g. OPay, PalmPay)
    CARD_SCHEME,        // e.g. Verve, Visa, Mastercard
    OFFLINE             // Manual or delayed settlements
}
