package com.netra.commons.requests.util;

import com.netra.commons.enums.TransactionChannel;
import com.netra.commons.enums.TransactionInstrument;
import lombok.Data;
@Data
public class TransactionRailDTO {
    private TransactionInstrument instrument;    // e.g., POS_DEVICE, ATM, MOBILE_APP
    private String instrumentId;    // e.g., Terminal ID, ATM ID, Device ID
    private TransactionChannel channel;   // e.g., POS_SWITCH, CARD_SCHEME
    private ChannelProviderSummaryDTO channelProvider;        // e.g., charmswitch
}

