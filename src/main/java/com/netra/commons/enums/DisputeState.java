package com.netra.commons.enums;

import java.util.EnumSet;
import java.util.Set;

public enum DisputeState {


    /* ---------------- PHASE 1: ENTERING POINT ---------------- */
    BOOTSTRAPING_DISPUTE_CONTEXT(Phase.INITIALIZATION, false, false),

    /* ---------------- PHASE 2: EVIDENCE VERIFICATION ---------------- */
    AWAITING_EVIDENCE_VERIFICATION(Phase.EVIDENCE_VERIFICATION, false, true),
    AWAITING_MANUAL_EVIDENCE_REVIEW(Phase.EVIDENCE_VERIFICATION, false, true),
    EVIDENCE_VERIFIED(Phase.EVIDENCE_VERIFICATION, false, true),
    EVIDENCE_REJECTED(Phase.EVIDENCE_VERIFICATION, false, true),

    /* ---------------- PHASE 3: ISSUER–ACQUIRER VERIFICATION ---------------- */
    AWAITING_ISSUER_VERIFICATION(Phase.PARTY_VERIFICATION, false, true),
    AWAITING_ISSUER_VERIFICATION_ISSUER_UNREACHABLE(Phase.PARTY_VERIFICATION, false, true),
    ISSUER_VERIFIED(Phase.PARTY_VERIFICATION, false, true),
    ISSUER_DECLINED(Phase.PARTY_VERIFICATION, false, true),

    AWAITING_ACQUIRER_VERIFICATION(Phase.PARTY_VERIFICATION, false, true),
    AWAITING_ACQUIRER_VERIFICATION_ISSUER_UNREACHABLE(Phase.PARTY_VERIFICATION, false, true),
    ACQUIRER_VERIFIED(Phase.PARTY_VERIFICATION, false, true),
    ACQUIRER_DECLINED(Phase.PARTY_VERIFICATION, false, true),

    /* ---------------- PHASE 4: ARBITRATION ---------------- */
    ARBITRATION_IN_PROGRESS(Phase.ARBITRATION, false, true),
    ARBITRATION_ISSUER_ESCALATED_MANUAL_REVIEW(Phase.ARBITRATION, false, true),
    ARBITRATION_ACQUIRER_ESCALATED_MANUAL_REVIEW(Phase.ARBITRATION, false, true),
    ARBITRATION_MANUAL_REVIEW_RESOLVED_ISSUER_FAVOR(Phase.ARBITRATION, true, true),
    ARBITRATION_MANUAL_REVIEW_RESOLVED_ACQUIRER_FAVOR(Phase.ARBITRATION, true, true),
    ARBITRATION_RESOLVED_ISSUER_FAVOR(Phase.ARBITRATION, true, true),
    ARBITRATION_RESOLVED_ACQUIRER_FAVOR(Phase.ARBITRATION, true, true),

    /* ---------------- PHASE 5: AUTHORITY ESCALATION ---------------- */
    AUTHORITY_ISSUER_ESCALATED(Phase.AUTHORITY, false, false),
    AUTHORITY_ACQUIRER_ESCALATED(Phase.AUTHORITY, false, false),
    LOCKED(Phase.AUTHORITY, false, false),

    /* ---------------- PHASE 6: WITHDRAWALS ---------------- */
    WITHDRAWN_CUSTOMER(Phase.CLOSURE, true, false),
    WITHDRAWN_ISSUER(Phase.CLOSURE, true, false),
    WITHDRAWN_ACQUIRER(Phase.CLOSURE, true, false),
    WITHDRAWN_ARBITRATION(Phase.CLOSURE, true, false),
    WITHDRAWN_AUTHORITY(Phase.CLOSURE, true, false),

    /* ---------------- PHASE 7: CLOSURE ---------------- */
    CLOSED(Phase.CLOSURE, true, false);

    /* ---------------- ENUM FIELDS ---------------- */
    private final Phase phase;
    private final boolean terminal;
    private final boolean representmentEligible;
    private final Set<DisputeState> nextStates;

    DisputeState(Phase phase, boolean terminal, boolean representmentEligible) {
        this.phase = phase;
        this.terminal = terminal;
        this.representmentEligible = representmentEligible;
        this.nextStates = EnumSet.noneOf(DisputeState.class);
    }

    static {
        /* ---- Evidence Flow ---- */
        AWAITING_EVIDENCE_VERIFICATION.nextStates.addAll(EnumSet.of(
                EVIDENCE_VERIFIED, EVIDENCE_REJECTED, AWAITING_MANUAL_EVIDENCE_REVIEW, WITHDRAWN_CUSTOMER
        ));
        AWAITING_MANUAL_EVIDENCE_REVIEW.nextStates.addAll(EnumSet.of(
                EVIDENCE_VERIFIED, EVIDENCE_REJECTED, WITHDRAWN_CUSTOMER
        ));
        EVIDENCE_VERIFIED.nextStates.addAll(EnumSet.of(
                AWAITING_ISSUER_VERIFICATION, AWAITING_ACQUIRER_VERIFICATION, WITHDRAWN_CUSTOMER
        ));
        EVIDENCE_REJECTED.nextStates.addAll(EnumSet.of(
                CLOSED, ARBITRATION_IN_PROGRESS, WITHDRAWN_CUSTOMER
        ));

        /* ---- Issuer/Acquirer Flow ---- */
        AWAITING_ISSUER_VERIFICATION.nextStates.addAll(EnumSet.of(
                ISSUER_VERIFIED, ISSUER_DECLINED, AWAITING_ISSUER_VERIFICATION_ISSUER_UNREACHABLE, WITHDRAWN_ISSUER
        ));
        AWAITING_ISSUER_VERIFICATION_ISSUER_UNREACHABLE.nextStates.addAll(EnumSet.of(
                AWAITING_ISSUER_VERIFICATION, ARBITRATION_IN_PROGRESS, WITHDRAWN_ISSUER
        ));
        ISSUER_VERIFIED.nextStates.addAll(EnumSet.of(
                AWAITING_ACQUIRER_VERIFICATION, CLOSED, WITHDRAWN_ISSUER
        ));
        ISSUER_DECLINED.nextStates.addAll(EnumSet.of(
                AWAITING_ACQUIRER_VERIFICATION, ARBITRATION_IN_PROGRESS, WITHDRAWN_ISSUER
        ));

        AWAITING_ACQUIRER_VERIFICATION.nextStates.addAll(EnumSet.of(
                ACQUIRER_VERIFIED, ACQUIRER_DECLINED, AWAITING_ACQUIRER_VERIFICATION_ISSUER_UNREACHABLE, WITHDRAWN_ACQUIRER
        ));
        AWAITING_ACQUIRER_VERIFICATION_ISSUER_UNREACHABLE.nextStates.addAll(EnumSet.of(
                AWAITING_ACQUIRER_VERIFICATION, ARBITRATION_IN_PROGRESS, WITHDRAWN_ACQUIRER
        ));
        ACQUIRER_VERIFIED.nextStates.addAll(EnumSet.of(
                CLOSED, ARBITRATION_IN_PROGRESS, WITHDRAWN_ACQUIRER
        ));
        ACQUIRER_DECLINED.nextStates.addAll(EnumSet.of(
                CLOSED, WITHDRAWN_ACQUIRER
        ));

        /* ---- Arbitration Flow ---- */
        ARBITRATION_IN_PROGRESS.nextStates.addAll(EnumSet.of(
                ARBITRATION_RESOLVED_ISSUER_FAVOR,
                ARBITRATION_RESOLVED_ACQUIRER_FAVOR,
                ARBITRATION_ISSUER_ESCALATED_MANUAL_REVIEW,
                ARBITRATION_ACQUIRER_ESCALATED_MANUAL_REVIEW,
                WITHDRAWN_ARBITRATION
        ));
        ARBITRATION_ISSUER_ESCALATED_MANUAL_REVIEW.nextStates.addAll(EnumSet.of(
                ARBITRATION_MANUAL_REVIEW_RESOLVED_ISSUER_FAVOR,
                ARBITRATION_MANUAL_REVIEW_RESOLVED_ACQUIRER_FAVOR,
                WITHDRAWN_ARBITRATION
        ));
        ARBITRATION_ACQUIRER_ESCALATED_MANUAL_REVIEW.nextStates.addAll(EnumSet.of(
                ARBITRATION_MANUAL_REVIEW_RESOLVED_ISSUER_FAVOR,
                ARBITRATION_MANUAL_REVIEW_RESOLVED_ACQUIRER_FAVOR,
                WITHDRAWN_ARBITRATION
        ));
        ARBITRATION_MANUAL_REVIEW_RESOLVED_ISSUER_FAVOR.nextStates.addAll(EnumSet.of(CLOSED));
        ARBITRATION_MANUAL_REVIEW_RESOLVED_ACQUIRER_FAVOR.nextStates.addAll(EnumSet.of(CLOSED));
        ARBITRATION_RESOLVED_ISSUER_FAVOR.nextStates.addAll(EnumSet.of(CLOSED));
        ARBITRATION_RESOLVED_ACQUIRER_FAVOR.nextStates.addAll(EnumSet.of(CLOSED));

        /* ---- Authority Flow ---- */
        AUTHORITY_ISSUER_ESCALATED.nextStates.addAll(EnumSet.of(LOCKED, CLOSED, WITHDRAWN_AUTHORITY));
        AUTHORITY_ACQUIRER_ESCALATED.nextStates.addAll(EnumSet.of(LOCKED, CLOSED, WITHDRAWN_AUTHORITY));

        /* ---- Locked & Closed ---- */
        LOCKED.nextStates.addAll(EnumSet.of(CLOSED));
        CLOSED.nextStates.addAll(EnumSet.noneOf(DisputeState.class));
    }

    public Phase getPhase() { return phase; }
    public boolean isTerminal() { return terminal; }
    public boolean isRepresentmentEligible() { return representmentEligible; }
    public Set<DisputeState> getNextStates() { return nextStates; }

    public enum Phase {
        INITIALIZATION,
        EVIDENCE_VERIFICATION,
        PARTY_VERIFICATION,
        ARBITRATION,
        AUTHORITY,
        CLOSURE
    }
}



