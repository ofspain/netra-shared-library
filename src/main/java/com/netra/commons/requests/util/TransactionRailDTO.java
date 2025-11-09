package com.netra.commons.requests.util;

import com.netra.commons.enums.Facilitator;
import com.netra.commons.enums.PaymentGateway;
import com.netra.commons.enums.PaymentRail;
import com.netra.commons.enums.TransactionInstrument;
import lombok.Data;
@Data
public class TransactionRailDTO {
    private TransactionInstrument instrument;
    private PaymentRail paymentRail;
    private PaymentGateway paymentGateway;
    private Facilitator facilitator;
    private String instrumentId;
}

