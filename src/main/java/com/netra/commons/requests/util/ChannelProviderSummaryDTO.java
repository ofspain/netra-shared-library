package com.netra.commons.requests.util;

import com.netra.commons.enums.DomainType;
import lombok.Data;

@Data
public class ChannelProviderSummaryDTO {
    private String name;            // e.g. "Interswitch"
    private String code;            // e.g. "SW001"
    private DomainType domainType = DomainType.SWITCH;  // SWITCH, FINANCIAL_INSTITUTION, etc.
}
