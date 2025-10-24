package com.netra.commons.enums;

public enum TransactionInstrument {
    CARD,               // Physical or virtual card
    USSD,               // *Dial-based transactions*
    MOBILE_APP,         // Mobile app channel
    WEB_PORTAL,         // Web app or Internet banking
    POS_DEVICE,         // POS terminal device
    ATM,                // ATM machine
    QR_CODE,            // QR-based payment
    WALLET,             // Wallet app or stored value account
    AGENCY_BANKING,     // Transactions via an agent network
    OFFLINE             // Offline/manual transaction
}
