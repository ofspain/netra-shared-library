package com.netra.commons.enums;

public enum PaymentRail {
    CARD,           // Visa, Mastercard, Verve, UnionPay, etc. (ISO 8583)
    ACCOUNT,        // Bank-to-bank or instant payment (e.g., NIP, RTP, SEPA)
    WALLET,         // Mobile money or fintech wallet
    HYBRID         // Mixed or indirect (e.g., wallet funded by card)
}

