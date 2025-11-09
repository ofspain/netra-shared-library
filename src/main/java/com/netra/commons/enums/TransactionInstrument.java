package com.netra.commons.enums;

public enum TransactionInstrument {
    USSD,               // *Dial-based transactions*
    MOBILE_APP,         // Mobile app channel
    WEB_PORTAL,         // Web app or Internet banking
    POS,         // POS terminal device
    ATM,                // ATM machine
    QR_CODE            // QR-based payment
}
