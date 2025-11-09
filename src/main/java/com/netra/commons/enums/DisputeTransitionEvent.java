package com.netra.commons.enums;
/**
 * Unified and role-agnostic dispute transition events.
 * These describe *what happened* in the lifecycle, not *who* did it,
 * allowing platform-neutral usage across issuer/acquirer/mobile/wallet disputes.
 */
public enum DisputeTransitionEvent {

    /* ---- Context bootstrap ---- */
    BOOTSTRAP_CONTEXT_USER,          // works for ordinary user
    BOOTSTRAP_CONTEXT_INSTITUTION,   // works only for institution
    BOOTSTRAP_CONTEXT_SUB_INSTITUTION, // works only for sub institution
    BOOTSTRAP_CONTEXT,               // works for everyone

    /* ---- Evidence lifecycle ---- */
    VALID_EVIDENCE,
    INVALID_EVIDENCE,

    /* ---- Dispute lifecycle ---- */
    START_DISPUTE,
    REQUEST_REVIEW,                 // replaces REQUEST_ISSUER; neutral term

    PLAINTIFF_VERIFIES,
    PLAINTIFF_DECLINES,

    DEFENDANT_VERIFIES,
    DEFENDANT_DECLINES,

    ESCALATE,
    RESOLVE,
    CLOSE,
    EXPIRES,

    /* ---- Withdrawals ---- */
    CUSTOMER_WITHDRAWS,
    PLAINTIFF_WITHDRAWS,
    DEFENDANT_WITHDRAWS,
    SUB_INSTITUTION_WITHDRAWS;
}
