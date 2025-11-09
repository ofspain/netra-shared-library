package com.netra.commons.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Comprehensive Dispute Lifecycle State Enum (role-neutral).
 * Each state optionally defines blockchain anchoring metadata
 * for on-chain event notarization on Aptos.
 */
public enum DisputeState {

    /* ---------------- PHASE 1: BOOTSTRAP ---------------- */
    BOOTSTRAP_DISPUTE_CONTEXT(Phase.INITIALIZATION, true, BlockchainEventType.DISPUTE_CREATED),

    /* ---------------- PHASE 2: EVIDENCE VERIFICATION ---------------- */
    AWAITING_EVIDENCE_VERIFICATION(Phase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_SUBMITTED),
    AWAITING_MANUAL_EVIDENCE_REVIEW(Phase.EVIDENCE_VERIFICATION, false, null),
    EVIDENCE_VERIFIED(Phase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_ACCEPTED),
    EVIDENCE_REJECTED(Phase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_REJECTED),

    /* ---------------- PHASE 3: PARTY VERIFICATION ---------------- */
    AWAITING_PLAINTIFF_VERIFICATION(Phase.PARTY_VERIFICATION, false, null),
    PLAINTIFF_VERIFIED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.PLAINTIFF_VERIFIED),
    PLAINTIFF_DECLINED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.PLAINTIFF_DECLINED),

    AWAITING_DEFENDANT_VERIFICATION(Phase.PARTY_VERIFICATION, false, null),
    DEFENDANT_VERIFIED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.DEFENDANT_VERIFIED),
    DEFENDANT_DECLINED(Phase.PARTY_VERIFICATION, true, BlockchainEventType.DEFENDANT_DECLINED),

    /* ---------------- PHASE 4: ARBITRATION PROCESSOR ---------------- */
    ARBITRATION_PROCESSOR_AWAITING_RESPONSE(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_INITIATED),
    ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_RULED_PLAINTIFF),
    ARBITRATION_PROCESSOR_RESOLVED_DEFENDANT_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_RULED_DEFENDANT),
    ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS(Phase.ARBITRATION, true, BlockchainEventType.ARBITRATION_AMBIGUOUS),
    ARBITRATION_PROCESSOR_UNREACHABLE(Phase.ARBITRATION, false, null),

    /* ---------------- PHASE 5: MANUAL ARBITRATION REVIEW ---------------- */
    ARBITRATION_AWAITING_MANUAL_REVIEW(Phase.ARBITRATION, false, null),
    ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_RULED_PLAINTIFF),
    ARBITRATION_MANUAL_RESOLVED_DEFENDANT_FAVOR(Phase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_RULED_DEFENDANT),
    ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS(Phase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_AMBIGUOUS),

    /* ---------------- PHASE 6: AUTHORITY ESCALATION ---------------- */
    AUTHORITY_PLAINTIFF_ESCALATED(Phase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_PLAINTIFF),
    AUTHORITY_DEFENDANT_ESCALATED(Phase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_DEFENDANT),
    AUTHORITY_SYSTEM_ESCALATED(Phase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_SYSTEM),

    /* ---------------- PHASE 7: WITHDRAWALS ---------------- */
    WITHDRAWN_CUSTOMER(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_CUSTOMER),
    WITHDRAWN_SUBDOMAIN(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_SUBDOMAIN),
    WITHDRAWN_PLAINTIFF(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_PLAINTIFF),
    WITHDRAWN_DEFENDANT(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_DEFENDANT),
    WITHDRAWN_ARBITRATION(Phase.CLOSURE, true, BlockchainEventType.WITHDRAWN_DURING_ARBITRATION),

    /* ---------------- PHASE 8: CLOSURE ---------------- */
    EXPIRED(Phase.CLOSURE, true, BlockchainEventType.DISPUTE_EXPIRED),
    CLOSED(Phase.CLOSURE, true, BlockchainEventType.DISPUTE_CLOSED);

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
        /* ---- Phase 1: Bootstrap ---- */
        BOOTSTRAP_DISPUTE_CONTEXT.nextStates.addAll(EnumSet.of(
                AWAITING_EVIDENCE_VERIFICATION,
                AWAITING_DEFENDANT_VERIFICATION
        ));

        /* ---- Phase 2: Evidence Verification ---- */
        AWAITING_EVIDENCE_VERIFICATION.nextStates.addAll(EnumSet.of(
                EVIDENCE_VERIFIED,
                EVIDENCE_REJECTED,
                AWAITING_MANUAL_EVIDENCE_REVIEW,
                WITHDRAWN_CUSTOMER,
                EXPIRED
        ));
        AWAITING_MANUAL_EVIDENCE_REVIEW.nextStates.addAll(EnumSet.of(
                EVIDENCE_VERIFIED,
                EVIDENCE_REJECTED,
                WITHDRAWN_CUSTOMER,
                EXPIRED
        ));
        EVIDENCE_VERIFIED.nextStates.addAll(EnumSet.of(
                AWAITING_PLAINTIFF_VERIFICATION,
                WITHDRAWN_CUSTOMER,
                EXPIRED
        ));
        EVIDENCE_REJECTED.nextStates.addAll(EnumSet.of(
                CLOSED,
                WITHDRAWN_CUSTOMER
        ));

        /* ---- Phase 3: Party Verification ---- */
        AWAITING_PLAINTIFF_VERIFICATION.nextStates.addAll(EnumSet.of(
                PLAINTIFF_VERIFIED,
                PLAINTIFF_DECLINED,
                WITHDRAWN_PLAINTIFF,
                EXPIRED
        ));
        PLAINTIFF_VERIFIED.nextStates.addAll(EnumSet.of(
                AWAITING_DEFENDANT_VERIFICATION,
                WITHDRAWN_PLAINTIFF,
                EXPIRED
        ));
        PLAINTIFF_DECLINED.nextStates.addAll(EnumSet.of(
                CLOSED,
                ARBITRATION_PROCESSOR_AWAITING_RESPONSE,
                WITHDRAWN_PLAINTIFF
        ));

        AWAITING_DEFENDANT_VERIFICATION.nextStates.addAll(EnumSet.of(
                DEFENDANT_VERIFIED,
                DEFENDANT_DECLINED,
                WITHDRAWN_DEFENDANT,
                EXPIRED
        ));
        DEFENDANT_VERIFIED.nextStates.addAll(EnumSet.of(
                CLOSED,
                EXPIRED
        ));
        DEFENDANT_DECLINED.nextStates.addAll(EnumSet.of(
                ARBITRATION_PROCESSOR_AWAITING_RESPONSE,
                WITHDRAWN_DEFENDANT
        ));

        /* ---- Phase 4: Arbitration Processor ---- */
        ARBITRATION_PROCESSOR_AWAITING_RESPONSE.nextStates.addAll(EnumSet.of(
                ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR,
                ARBITRATION_PROCESSOR_RESOLVED_DEFENDANT_FAVOR,
                ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS,
                ARBITRATION_PROCESSOR_UNREACHABLE,
                WITHDRAWN_ARBITRATION,
                EXPIRED
        ));
        ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_DEFENDANT_ESCALATED,
                CLOSED,
                WITHDRAWN_ARBITRATION
        ));
        ARBITRATION_PROCESSOR_RESOLVED_DEFENDANT_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_PLAINTIFF_ESCALATED,
                CLOSED,
                WITHDRAWN_ARBITRATION
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

        /* ---- Phase 5: Manual Arbitration Review ---- */
        ARBITRATION_AWAITING_MANUAL_REVIEW.nextStates.addAll(EnumSet.of(
                ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR,
                ARBITRATION_MANUAL_RESOLVED_DEFENDANT_FAVOR,
                ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS,
                EXPIRED
        ));
        ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_PLAINTIFF_ESCALATED,
                CLOSED,
                WITHDRAWN_ARBITRATION
        ));
        ARBITRATION_MANUAL_RESOLVED_DEFENDANT_FAVOR.nextStates.addAll(EnumSet.of(
                AUTHORITY_DEFENDANT_ESCALATED,
                CLOSED,
                WITHDRAWN_ARBITRATION
        ));
        ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS.nextStates.addAll(EnumSet.of(
                AUTHORITY_SYSTEM_ESCALATED,
                CLOSED
        ));

        /* ---- Phase 6: Authority Escalation ---- */
        AUTHORITY_PLAINTIFF_ESCALATED.nextStates.addAll(EnumSet.of(CLOSED));
        AUTHORITY_DEFENDANT_ESCALATED.nextStates.addAll(EnumSet.of(CLOSED));
        AUTHORITY_SYSTEM_ESCALATED.nextStates.addAll(EnumSet.of(CLOSED));

        /* ---- Phase 7: Withdrawals ---- */
        WITHDRAWN_CUSTOMER.nextStates.addAll(EnumSet.of(CLOSED));
        WITHDRAWN_SUBDOMAIN.nextStates.addAll(EnumSet.of(CLOSED));
        WITHDRAWN_PLAINTIFF.nextStates.addAll(EnumSet.of(CLOSED));
        WITHDRAWN_DEFENDANT.nextStates.addAll(EnumSet.of(CLOSED));
        WITHDRAWN_ARBITRATION.nextStates.addAll(EnumSet.of(CLOSED));

        /* ---- Phase 8: Closure ---- */
        EXPIRED.nextStates.addAll(EnumSet.of(CLOSED));
        CLOSED.nextStates.addAll(EnumSet.noneOf(DisputeState.class));
    }

    public Phase getPhase() { return phase; }
    public Set<DisputeState> getNextStates() { return nextStates; }
    public boolean isBlockchainLoggable() { return isBlockchainLoggable; }
    public BlockchainEventType getBlockchainEventType() { return blockchainEventType; }

    public enum Phase {
        INITIALIZATION,
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
        DEFENDANT_VERIFIED,
        DEFENDANT_DECLINED,
        ARBITRATION_INITIATED,
        ARBITRATION_RULED_PLAINTIFF,
        ARBITRATION_RULED_DEFENDANT,
        ARBITRATION_AMBIGUOUS,
        MANUAL_ARBITRATION_RULED_PLAINTIFF,
        MANUAL_ARBITRATION_RULED_DEFENDANT,
        MANUAL_ARBITRATION_AMBIGUOUS,
        AUTHORITY_ESCALATED_PLAINTIFF,
        AUTHORITY_ESCALATED_DEFENDANT,
        AUTHORITY_ESCALATED_SYSTEM,
        WITHDRAWN_BY_CUSTOMER,
        WITHDRAWN_BY_SUBDOMAIN,
        WITHDRAWN_BY_PLAINTIFF,
        WITHDRAWN_BY_DEFENDANT,
        WITHDRAWN_DURING_ARBITRATION,
        DISPUTE_EXPIRED,
        DISPUTE_CLOSED
    }
}
