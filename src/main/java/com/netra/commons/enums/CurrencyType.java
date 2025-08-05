package com.netra.commons.enums;

// --- Enum for currency types ---
public enum CurrencyType {
    //normal currency like NGN
    FIAT,

    //decentralized currency
    CRYPTO,
    //Central Bank Digital Currency like eNaira
    CBDC,

    //Crypto backed by government eg circle
    STABLE_COIN,

    //app own currency
    TOKEN
}
