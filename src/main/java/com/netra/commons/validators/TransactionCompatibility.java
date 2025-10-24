package com.netra.commons.validators;

import com.netra.commons.enums.TransactionProcessor;
import com.netra.commons.enums.TransactionInstrument;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class TransactionCompatibility {

    private static final Map<TransactionInstrument, Set<TransactionProcessor>> COMPATIBILITY_MAP =
            new EnumMap<>(TransactionInstrument.class);

    static {
        COMPATIBILITY_MAP.put(TransactionInstrument.CARD,
                EnumSet.of(TransactionProcessor.POS_SWITCH, TransactionProcessor.CARD_SCHEME, TransactionProcessor.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.USSD,
                EnumSet.of(TransactionProcessor.NIP, TransactionProcessor.USSD_GATEWAY, TransactionProcessor.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.MOBILE_APP,
                EnumSet.of(TransactionProcessor.NIP, TransactionProcessor.WALLET_PROCESSOR,
                        TransactionProcessor.CARD_SCHEME, TransactionProcessor.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.WEB_PORTAL,
                EnumSet.of(TransactionProcessor.NIP, TransactionProcessor.WALLET_PROCESSOR,
                        TransactionProcessor.CARD_SCHEME, TransactionProcessor.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.POS_DEVICE,
                EnumSet.of(TransactionProcessor.POS_SWITCH, TransactionProcessor.CARD_SCHEME, TransactionProcessor.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.ATM,
                EnumSet.of(TransactionProcessor.POS_SWITCH, TransactionProcessor.CARD_SCHEME, TransactionProcessor.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.QR_CODE,
                EnumSet.of(TransactionProcessor.NIP, TransactionProcessor.WALLET_PROCESSOR,
                        TransactionProcessor.CARD_SCHEME, TransactionProcessor.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.WALLET,
                EnumSet.of(TransactionProcessor.NIP, TransactionProcessor.USSD_GATEWAY,
                        TransactionProcessor.WALLET_PROCESSOR, TransactionProcessor.OFFLINE));
    }

    public static boolean isCompatible(TransactionInstrument instrument, TransactionProcessor channel) {
        return COMPATIBILITY_MAP.getOrDefault(instrument, EnumSet.noneOf(TransactionProcessor.class))
                .contains(channel);
    }
}
