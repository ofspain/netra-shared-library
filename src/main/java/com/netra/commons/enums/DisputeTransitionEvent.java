package com.netra.commons.enums;
/**
 * Unified and role-agnostic dispute transition events.
 * These describe *what happened* in the lifecycle, not *who* did it,
 * allowing platform-neutral usage across issuer/acquirer/mobile/wallet disputes.
 */
public enum DisputeTransitionEvent {

    /* ---- Context bootstrap ---- */
    EVENT_BOOTSTRAP_CONTEXT_USER,          // works for ordinary user
    EVENT_BOOTSTRAP_CONTEXT_INSTITUTION,   // works only for institution
    EVENT_BOOTSTRAP_CONTEXT_SUB_INSTITUTION, // works only for sub institution
    EVENT_BOOTSTRAP_CONTEXT,               // works for everyone

    /* ---- Evidence lifecycle ---- */
    EVENT_EVIDENCE_PROCESSED,

    EVENT_REQUEST_REVIEW,                 // replaces REQUEST_ISSUER; neutral term

    EVENT_PLAINTIFF_PROCESSED,

    EVENT_RESPONDER_PROCESSED,
    EVENT_ESCALATE,
    EVENT_RESOLVE,
    EVENT_CLOSE,
    EVENT_EXPIRES,

    /* ---- Withdrawals ---- */
    EVENT_CUSTOMER_WITHDRAWS,
    EVENT_PLAINTIFF_WITHDRAWS,
    EVENT_RESPONDER_WITHDRAWS,
    EVENT_SUB_INSTITUTION_WITHDRAWS,

    /* ---------------------------------------------------
     *  TIME & REACHABILITY EVENTS
     * --------------------------------------------------- */
    EVENT_EXPIRED,
    EVENT_PLAINTIFF_UNREACHABLE,
    EVENT_RESPONDER_UNREACHABLE,
    EVENT_PROCESSOR_UNREACHABLE,

    /* ---------------------------------------------------
     *  ARBITRATION PROCESSOR EVENTS
     * --------------------------------------------------- */
    EVENT_ARBITRATION_RULES_PLAINTIFF,
    EVENT_ARBITRATION_RULES_RESPONDER,
    EVENT_ARBITRATION_AMBIGUOUS,
    EVENT_ARBITRATION_UNREACHABLE,

    /* --------------------TODO...starts refactoring from here-------------------------------
     *  MANUAL ARBITRATION REVIEW EVENTS
     * --------------------------------------------------- */
    EVENT_MANUAL_REVIEW_RULES_PLAINTIFF,
    EVENT_MANUAL_REVIEW_RULES_RESPONDER,
    EVENT_MANUAL_REVIEW_AMBIGUOUS,

    /* ---------------------------------------------------
     *  AUTHORITY / FINAL VERDICT EVENTS
     * --------------------------------------------------- */
    EVENT_AUTHORITY_ESCALATES,
    EVENT_AUTHORITY_RULES_PLAINTIFF,
    EVENT_AUTHORITY_RULES_RESPONDER,
    EVENT_AUTHORITY_AMBIGUOUS,

    /* ---------------------------------------------------
     *  SYSTEM ACTION / META EVENTS
     * --------------------------------------------------- */
    EVENT_SYSTEM_AUTO_CLOSES,
    EVENT_SYSTEM_AUTO_ESCALATES
}
