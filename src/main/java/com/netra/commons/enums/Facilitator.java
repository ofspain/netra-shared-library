package com.netra.commons.enums;

public enum Facilitator {
    AGENT, //formay agency banking facilitator
    SUB_AGENT, //think of this as random agent or one-off pos devices operator that uses this as form of biz
    MERCHANT, //standard merchant that is registered with a standard acquirer or aggregator
    SUB_MERCHANT,//think of this as small vendors or retailers but uses pos for example as payment method
}
//small vendors, random agents, one-off POS devices