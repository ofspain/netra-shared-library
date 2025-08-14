package com.netra.commons.validators;

import com.netra.commons.enums.TransactionChannel;
import com.netra.commons.enums.TransactionInstrument;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class TransactionCompatibility {

    private static final Map<TransactionInstrument, Set<TransactionChannel>> COMPATIBILITY_MAP =
            new EnumMap<>(TransactionInstrument.class);

    static {
        COMPATIBILITY_MAP.put(TransactionInstrument.CARD,
                EnumSet.of(TransactionChannel.POS_SWITCH, TransactionChannel.CARD_SCHEME, TransactionChannel.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.USSD,
                EnumSet.of(TransactionChannel.NIP, TransactionChannel.USSD_GATEWAY, TransactionChannel.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.MOBILE_APP,
                EnumSet.of(TransactionChannel.NIP, TransactionChannel.WALLET_PROCESSOR,
                        TransactionChannel.CARD_SCHEME, TransactionChannel.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.WEB_PORTAL,
                EnumSet.of(TransactionChannel.NIP, TransactionChannel.WALLET_PROCESSOR,
                        TransactionChannel.CARD_SCHEME, TransactionChannel.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.POS_DEVICE,
                EnumSet.of(TransactionChannel.POS_SWITCH, TransactionChannel.CARD_SCHEME, TransactionChannel.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.ATM,
                EnumSet.of(TransactionChannel.POS_SWITCH, TransactionChannel.CARD_SCHEME, TransactionChannel.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.QR_CODE,
                EnumSet.of(TransactionChannel.NIP, TransactionChannel.WALLET_PROCESSOR,
                        TransactionChannel.CARD_SCHEME, TransactionChannel.OFFLINE));

        COMPATIBILITY_MAP.put(TransactionInstrument.WALLET,
                EnumSet.of(TransactionChannel.NIP, TransactionChannel.USSD_GATEWAY,
                        TransactionChannel.WALLET_PROCESSOR, TransactionChannel.OFFLINE));
    }

    public static boolean isCompatible(TransactionInstrument instrument, TransactionChannel channel) {
        return COMPATIBILITY_MAP.getOrDefault(instrument, EnumSet.noneOf(TransactionChannel.class))
                .contains(channel);
    }
}
