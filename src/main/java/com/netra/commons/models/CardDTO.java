package com.netra.commons.models;

import lombok.Data;

@Data
public class CardDTO {
    private CardScheme cardScheme;
    private String firstSixDigits;//BIN stands for Bank Identification Number — although the formal ISO term now is Issuer Identification Number (IIN).
    private String lastFourDigits;
}
