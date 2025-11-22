package com.netra.commons.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Simplified Dispute Lifecycle State Enum with hierarchical support.
 * Parent states allow global transitions (like withdrawals) without duplication.
 */
public enum DisputeState {

    /* ========== PARENT STATES ========== */

    // 🏠 ACTIVE - All ongoing processing states
    ACTIVE(Phase.ACTIVE, false, null),

    // 🏠 TERMINAL - All final states
    TERMINAL(Phase.CLOSURE, false, null),

    // 🏠 WITHDRAWN - All withdrawal states
    WITHDRAWN(Phase.CLOSURE, false, null),


    /* ========== ACTIVE CHILD STATES ========== */

    /* ---------------- INITIALIZATION ---------------- */
    BOOTSTRAP_DISPUTE_CONTEXT(Phase.INITIALIZATION, true, BlockchainEventType.DISPUTE_CREATED),

    /* ---------------- EVIDENCE VERIFICATION ---------------- */
    AWAITING_EVIDENCE_VERIFICATION(Phase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_SUBMITTED),
    AWAITING_MANUAL_EVIDENCE_REVIEW(Phase.EVIDENCE_VERIFICATION, false, null),
    EVIDENCE_VERIFIED(Phase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_ACCEPTED),
    EVIDENCE_REJECTED(Phase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_REJECTED),

    /* ---------------- PARTY VERIFICATION ---------------- */
    AWAITING_PLAINTIFF_VERIFICATION(Phase.PARTY_VERIFICATION, false, null),
    PLAINTIFF_VERIFIED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.PLAINTIFF_VERIFIED),
    PLAINTIFF_DECLINED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.PLAINTIFF_DECLINED),

    AWAITING_RESPONDER_VERIFICATION(Phase.PARTY_VERIFICATION, false, null),
    RESPONDER_VERIFIED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.RESPONDER_VERIFIED),
    RESPONDER_DECLINED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.RESPONDER_DECLINED),

    /* ---------------- ARBITRATION ---------------- */
    ARBITRATION_PROCESSOR_AWAITING_RESPONSE(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_INITIATED),
    ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_RULED_PLAINTIFF),
    ARBITRATION_PROCESSOR_RESOLVED_RESPONDER_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_RULED_RESPONDER),
    ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_AMBIGUOUS),
    ARBITRATION_PROCESSOR_UNREACHABLE(Phase.ARBITRATION, false, null),

    ARBITRATION_AWAITING_MANUAL_REVIEW(Phase.ARBITRATION, false, null),
    ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_RULED_PLAINTIFF),
    ARBITRATION_MANUAL_RESOLVED_RESPONDER_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_RULED_RESPONDER),
    ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS(Phase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_AMBIGUOUS),

    /* ---------------- AUTHORITY ESCALATION ---------------- */
    AUTHORITY_PLAINTIFF_ESCALATED(Phase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_PLAINTIFF),
    AUTHORITY_RESPONDER_ESCALATED(Phase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_RESPONDER),
    AUTHORITY_SYSTEM_ESCALATED(Phase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_SYSTEM),

    /* ---------------- EXPIRATION ---------------- */
    EXPIRED(Phase.CLOSURE, true, BlockchainEventType.DISPUTE_EXPIRED),


    /* ========== TERMINAL CHILD STATES ========== */
    CLOSED(Phase.CLOSURE, true, BlockchainEventType.DISPUTE_CLOSED),


    /* ========== WITHDRAWN CHILD STATES ========== */
    WITHDRAWN_CUSTOMER(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_CUSTOMER),
    WITHDRAWN_PLAINTIFF(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_PLAINTIFF),
    WITHDRAWN_RESPONDER(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_RESPONDER),
    WITHDRAWN_SUB_INSTITUTION(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_SUB_INSTITUTION),
    WITHDRAWN_ARBITRATION(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_DURING_ARBITRATION);

    private final Phase phase;
    private final Set<DisputeState> nextStates;
    private final boolean isBlockchainLoggable;
    private final BlockchainEventType blockchainEventType;

    DisputeState(Phase phase, boolean isBlockchainLoggable, BlockchainEventType blockchainEventType) {
        this.phase = phase;
        this.isBlockchainLoggable = isBlockchainLoggable;
        this.blockchainEventType = blockchainEventType;
        this.nextStates = EnumSet.noneOf(DisputeState.class);
    }

    static {
        /* ========== BUSINESS TRANSITIONS ========== */

        /* ---- Initialization ---- */
        BOOTSTRAP_DISPUTE_CONTEXT.nextStates.addAll(EnumSet.of(
                AWAITING_EVIDENCE_VERIFICATION,
                AWAITING_RESPONDER_VERIFICATION
        ));

        /* ---- Evidence Verification ---- */
        AWAITING_EVIDENCE_VERIFICATION.nextStates.addAll(EnumSet.of(
                EVIDENCE_VERIFIED,
                EVIDENCE_REJECTED,
                AWAITING_MANUAL_EVIDENCE_REVIEW,
                EXPIRED
        ));

        AWAITING_MANUAL_EVIDENCE_REVIEW.nextStates.addAll(EnumSet.of(
                EVIDENCE_VERIFIED,
                EVIDENCE_REJECTED,
                EXPIRED
        ));

        EVIDENCE_VERIFIED.nextStates.addAll(EnumSet.of(
                AWAITING_PLAINTIFF_VERIFICATION,
                EXPIRED
        ));

        EVIDENCE_REJECTED.nextStates.addAll(EnumSet.of(
                CLOSED
        ));

        /* ---- Party Verification ---- */
        AWAITING_PLAINTIFF_VERIFICATION.nextStates.addAll(EnumSet.of(
                PLAINTIFF_VERIFIED,
                PLAINTIFF_DECLINED,
                EXPIRED
        ));

        PLAINTIFF_VERIFIED.nextStates.addAll(EnumSet.of(
                AWAITING_RESPONDER_VERIFICATION,
                EXPIRED
        ));

        PLAINTIFF_DECLINED.nextStates.addAll(EnumSet.of(
                CLOSED,
                ARBITRATION_PROCESSOR_AWAITING_RESPONSE
        ));

        AWAITING_RESPONDER_VERIFICATION.nextStates.addAll(EnumSet.of(
                RESPONDER_VERIFIED,
                RESPONDER_DECLINED,
                EXPIRED
        ));

        RESPONDER_VERIFIED.nextStates.addAll(EnumSet.of(
                CLOSED,
                EXPIRED
        ));

        RESPONDER_DECLINED.nextStates.addAll(EnumSet.of(
                ARBITRATION_PROCESSOR_AWAITING_RESPONSE
        ));

        /* ---- Arbitration ---- */
        ARBITRATION_PROCESSOR_AWAITING_RESPONSE.nextStates.addAll(EnumSet.of(
                ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR,
                ARBITRATION_PROCESSOR_RESOLVED_RESPONDER_FAVOR,
                ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS,
                ARBITRATION_PROCESSOR_UNREACHABLE,
                EXPIRED
        ));

        ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_RESPONDER_ESCALATED,
                CLOSED
        ));

        ARBITRATION_PROCESSOR_RESOLVED_RESPONDER_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_PLAINTIFF_ESCALATED,
                CLOSED
        ));

        ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS.nextStates.addAll(EnumSet.of(
                ARBITRATION_AWAITING_MANUAL_REVIEW,
                AUTHORITY_SYSTEM_ESCALATED,
                CLOSED
        ));

        ARBITRATION_PROCESSOR_UNREACHABLE.nextStates.addAll(EnumSet.of(
                ARBITRATION_AWAITING_MANUAL_REVIEW,
                AUTHORITY_SYSTEM_ESCALATED,
                EXPIRED
        ));

        ARBITRATION_AWAITING_MANUAL_REVIEW.nextStates.addAll(EnumSet.of(
                ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR,
                ARBITRATION_MANUAL_RESOLVED_RESPONDER_FAVOR,
                ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS,
                EXPIRED
        ));

        ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_PLAINTIFF_ESCALATED,
                CLOSED
        ));

        ARBITRATION_MANUAL_RESOLVED_RESPONDER_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_RESPONDER_ESCALATED,
                CLOSED
        ));

        ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS.nextStates.addAll(EnumSet.of(
                AUTHORITY_SYSTEM_ESCALATED,
                CLOSED
        ));

        /* ---- Authority Escalation ---- */
        AUTHORITY_PLAINTIFF_ESCALATED.nextStates.addAll(EnumSet.of(CLOSED));
        AUTHORITY_RESPONDER_ESCALATED.nextStates.addAll(EnumSet.of(CLOSED));
        AUTHORITY_SYSTEM_ESCALATED.nextStates.addAll(EnumSet.of(CLOSED));

        /* ---- Withdrawals ---- */
        WITHDRAWN_CUSTOMER.nextStates.addAll(EnumSet.of(CLOSED));
        WITHDRAWN_PLAINTIFF.nextStates.addAll(EnumSet.of(CLOSED));
        WITHDRAWN_RESPONDER.nextStates.addAll(EnumSet.of(CLOSED));
        WITHDRAWN_ARBITRATION.nextStates.addAll(EnumSet.of(CLOSED));

        /* ---- Expiration ---- */
        EXPIRED.nextStates.addAll(EnumSet.of(CLOSED));

        /* ---- Terminal ---- */
        TERMINAL.nextStates.addAll(EnumSet.of(CLOSED));
        CLOSED.nextStates.addAll(EnumSet.noneOf(DisputeState.class));
    }

    /* ========== HELPER METHODS ========== */

    public boolean isActiveChild() {
        return getAllActiveChildren().contains(this);
    }

    public boolean isWithdrawnChild() {
        return getAllWithdrawnChildren().contains(this);
    }

    public boolean isTerminalChild() {
        return getAllTerminalChildren().contains(this);
    }

    public static Set<DisputeState> getAllActiveChildren() {
        return EnumSet.of(
                AWAITING_EVIDENCE_VERIFICATION, AWAITING_MANUAL_EVIDENCE_REVIEW,
                EVIDENCE_VERIFIED, EVIDENCE_REJECTED,
                AWAITING_PLAINTIFF_VERIFICATION, PLAINTIFF_VERIFIED, PLAINTIFF_DECLINED,
                AWAITING_RESPONDER_VERIFICATION, RESPONDER_VERIFIED, RESPONDER_DECLINED,
                ARBITRATION_PROCESSOR_AWAITING_RESPONSE, ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR,
                ARBITRATION_PROCESSOR_RESOLVED_RESPONDER_FAVOR, ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS,
                ARBITRATION_PROCESSOR_UNREACHABLE, ARBITRATION_AWAITING_MANUAL_REVIEW,
                ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR, ARBITRATION_MANUAL_RESOLVED_RESPONDER_FAVOR,
                ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS, AUTHORITY_PLAINTIFF_ESCALATED,
                AUTHORITY_RESPONDER_ESCALATED, AUTHORITY_SYSTEM_ESCALATED,
                EXPIRED
        );
    }

    public static Set<DisputeState> getAllTerminalChildren() {
        return EnumSet.of(CLOSED); // Only CLOSED is direct child of TERMINAL
    }

    public static Set<DisputeState> getAllWithdrawnChildren() {
        return EnumSet.of(
                WITHDRAWN_CUSTOMER, WITHDRAWN_PLAINTIFF,
                WITHDRAWN_RESPONDER, WITHDRAWN_ARBITRATION
        );
    }

    public Phase getPhase() { return phase; }
    public Set<DisputeState> getNextStates() { return nextStates; }
    public boolean isBlockchainLoggable() { return isBlockchainLoggable; }
    public BlockchainEventType getBlockchainEventType() { return blockchainEventType; }

    public enum Phase {
        INITIALIZATION,
        ACTIVE,
        EVIDENCE_VERIFICATION,
        PARTY_VERIFICATION,
        ARBITRATION,
        AUTHORITY,
        CLOSURE
    }

    public enum BlockchainEventType {
        DISPUTE_CREATED,
        EVIDENCE_SUBMITTED,
        EVIDENCE_ACCEPTED,
        EVIDENCE_REJECTED,
        PLAINTIFF_VERIFIED,
        PLAINTIFF_DECLINED,
        RESPONDER_VERIFIED,
        RESPONDER_DECLINED,
        ARBITRATION_INITIATED,
        ARBITRATION_RULED_PLAINTIFF,
        ARBITRATION_RULED_RESPONDER,
        ARBITRATION_AMBIGUOUS,
        MANUAL_ARBITRATION_RULED_PLAINTIFF,
        MANUAL_ARBITRATION_RULED_RESPONDER,
        MANUAL_ARBITRATION_AMBIGUOUS,
        AUTHORITY_ESCALATED_PLAINTIFF,
        AUTHORITY_ESCALATED_RESPONDER,
        AUTHORITY_ESCALATED_SYSTEM,
        WITHDRAWN_BY_CUSTOMER,
        WITHDRAWN_BY_PLAINTIFF,
        WITHDRAWN_BY_RESPONDER,
        WITHDRAWN_DURING_ARBITRATION,

        WITHDRAWN_BY_SUB_INSTITUTION,
        DISPUTE_EXPIRED,
        DISPUTE_CLOSED
    }
}