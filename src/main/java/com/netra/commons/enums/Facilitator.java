package com.netra.commons.enums;
/**
 * Represents the type of entity that facilitates a financial transaction.
 *
 * This enum is tailored to the Nigerian payments ecosystem, covering
 * retail, fintech, and institutional transactions. It helps enforce
 * business rules, routing, reporting, and auditing based on facilitator type.
 */
public enum Facilitator {

    /**
     * Formal banking agent operating under a licensed financial institution.
     * Typically part of an official branchless banking or agency banking network.
     * Example: GTBank agent, FirstBank agent.
     */
    AGENT,

    /**
     * Informal or independent agent (sub-agent) who operates under an official agent or aggregator.
     * Often handles small-scale or one-off transactions via POS or mobile platforms.
     * Example: a small POS operator under a larger agent.
     */
    SUB_AGENT,

    /**
     * Standard merchant registered with an acquirer or payment aggregator.
     * Accepts payments for goods or services in a commercial setting.
     * Example: supermarkets, online stores, registered shops.
     */
    MERCHANT,

    /**
     * Small-scale vendors or sub-merchants operating under a larger merchant or aggregator.
     * Often rely on POS or third-party infrastructure to accept payments.
     * Example: market stall using a POS from a bigger merchant.
     */
    SUB_MERCHANT,

    /**
     * Self-facilitated transaction initiated directly by the end-user.
     * Example: customer paying via mobile banking app, USSD transfer, or internet banking.
     */
    SELF,

    /**
     * Payment Service Provider (PSP) or fintech aggregator facilitating transactions.
     * Typically handles multiple merchants or agents and provides payment infrastructure.
     * Example: Paystack, Flutterwave, Paga.
     */
    PSP,

    /**
     * Institutional or government facilitator.
     * Used when a government agency, corporate entity, or institution initiates transactions.
     * Example: federal tax collection, school fees portal, pension contributions.
     */
    INSTITUTIONAL
}
