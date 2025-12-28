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
         //===> AWAITING_MANUAL_EVIDENCE_REVIEW, EVIDENCE_VERIFIED, EVIDENCE_REJECTED

    EVENT_REQUEST_REVIEW,                 // replaces REQUEST_ISSUER; neutral term

    EVENT_PLAINTIFF_PROCESSED,
         //==> PLAINTIFF_VERIFIED, PLAINTIFF_DECLINED, AWAITING_PLAINTIFF_VERIFICATION
    EVENT_RESPONDER_PROCESSED,
    //==> RESPONDER_VERIFIED, RESPONDER_DECLINED, AWAITING_RESPONDER_VERIFICATION
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
    EVENT_ARBITRATION_PROCESSOR_PROCESSED,
         //===>  ARBITRATION_PROCESSOR_RESOLVED_RESPONDER_FAVOR, ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR
         //===>  , ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS, ARBITRATION_PROCESSOR_UNREACHABLE

    /* --------------------TODO...starts refactoring from here-------------------------------
     *  MANUAL ARBITRATION REVIEW EVENTS
     * --------------------------------------------------- */
    EVENT_MANUAL_REVIEW_PROCESSED,
          //ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR, ARBITRATION_MANUAL_RESOLVED_RESPONDER_FAVOR,ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS
    /* ---------------------------------------------------
     *  AUTHORITY / FINAL VERDICT EVENTS
     * --------------------------------------------------- */
    EVENT_ESCALATES_AUTHORITY,
        //====>AUTHORITY_PLAINTIFF_ESCALATED, AUTHORITY_RESPONDER_ESCALATED, AUTHORITY_SYSTEM_ESCALATED
    EVENT_AUTHORITY_PROCESSED,
    //====>

    /* ---------------------------------------------------
     *  SYSTEM ACTION / META EVENTS
     * --------------------------------------------------- */
    EVENT_SYSTEM_AUTO_CLOSES;
}
