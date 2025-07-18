package com.netra.commons.models;

import lombok.Data;

@Data
public class CardDTO {
    private CardScheme cardScheme;
    private String firstFourDigits;
    private String lastFourDigits;
}
