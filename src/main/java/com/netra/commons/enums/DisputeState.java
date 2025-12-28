package com.netra.commons.enums;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum DisputeState {
    ACTIVE(TransitionPhase.ACTIVE, false, null),
    ACTIVE_ENTRY(TransitionPhase.ACTIVE, false, null),
    TERMINAL(TransitionPhase.CLOSURE, false, null),
    TERMINAL_ENTRY(TransitionPhase.CLOSURE, false, null),
    WITHDRAWN(TransitionPhase.CLOSURE, false, null),
    WITHDRAWN_ENTRY(TransitionPhase.CLOSURE, false, null),

    BOOTSTRAP_FAILED(TransitionPhase.INITIALIZATION, false, null),

    BOOTSTRAP_DISPUTE_CONTEXT(TransitionPhase.INITIALIZATION, true, BlockchainEventType.DISPUTE_CREATED_BCE),
    AWAITING_EVIDENCE_VERIFICATION(TransitionPhase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_SUBMITTED_BCE),
    AWAITING_MANUAL_EVIDENCE_REVIEW(TransitionPhase.EVIDENCE_VERIFICATION, false, null),
    EVIDENCE_VERIFIED(TransitionPhase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_ACCEPTED_BCE),
    EVIDENCE_REJECTED(TransitionPhase.EVIDENCE_VERIFICATION, true, BlockchainEventType.EVIDENCE_REJECTED_BCE),
    REQUEST_PLAINTIFF_VERIFICATION(TransitionPhase.PARTY_VERIFICATION, false, null),
    AWAITING_PLAINTIFF_VERIFICATION(TransitionPhase.PARTY_VERIFICATION, false, null),
    PLAINTIFF_VERIFIED(TransitionPhase.PARTY_VERIFICATION, true, BlockchainEventType.PLAINTIFF_VERIFIED_BCE),
    PLAINTIFF_DECLINED(TransitionPhase.PARTY_VERIFICATION, true, BlockchainEventType.PLAINTIFF_DECLINED_BCE),
    REQUEST_RESPONDER_VERIFICATION(TransitionPhase.PARTY_VERIFICATION, false, null),
    AWAITING_RESPONDER_VERIFICATION(TransitionPhase.PARTY_VERIFICATION, false, null),
    RESPONDER_VERIFIED(TransitionPhase.PARTY_VERIFICATION, true, BlockchainEventType.RESPONDER_VERIFIED_BCE),
    RESPONDER_DECLINED(TransitionPhase.PARTY_VERIFICATION, true, BlockchainEventType.RESPONDER_DECLINED_BCE),
    REQUEST_PROCESSOR_ARBITRATION(TransitionPhase.ARBITRATION, false, null),
    ARBITRATION_PROCESSOR_AWAITING_RESPONSE(TransitionPhase.ARBITRATION, true, BlockchainEventType.ARBITRATION_INITIATED_BCE),
    ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR(TransitionPhase.ARBITRATION, true, BlockchainEventType.ARBITRATION_RULED_PLAINTIFF_BCE),
    ARBITRATION_PROCESSOR_RESOLVED_RESPONDER_FAVOR(TransitionPhase.ARBITRATION, true, BlockchainEventType.ARBITRATION_RULED_RESPONDER_BCE),
    ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS(TransitionPhase.ARBITRATION, true, BlockchainEventType.ARBITRATION_AMBIGUOUS_BCE),
    REQUEST_MANUAL_ARBITRATION(TransitionPhase.ARBITRATION, false, null),
    ARBITRATION_AWAITING_MANUAL_REVIEW(TransitionPhase.ARBITRATION, false, null),
    ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR(TransitionPhase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_RULED_PLAINTIFF_BCE),
    ARBITRATION_MANUAL_RESOLVED_RESPONDER_FAVOR(TransitionPhase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_RULED_RESPONDER_BCE),
    ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS(TransitionPhase.ARBITRATION, true, BlockchainEventType.MANUAL_ARBITRATION_AMBIGUOUS_BCE),
    AUTHORITY_PLAINTIFF_ESCALATED(TransitionPhase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_PLAINTIFF_BCE),
    AUTHORITY_RESPONDER_ESCALATED(TransitionPhase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_RESPONDER_BCE),
    AUTHORITY_SYSTEM_ESCALATED(TransitionPhase.AUTHORITY, true, BlockchainEventType.AUTHORITY_ESCALATED_SYSTEM_BCE),
    EXPIRED(TransitionPhase.CLOSURE, true, BlockchainEventType.DISPUTE_EXPIRED_BCE),
    CLOSED(TransitionPhase.CLOSURE, true, BlockchainEventType.DISPUTE_CLOSED_BCE),
    WITHDRAWN_CUSTOMER(TransitionPhase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_CUSTOMER_BCE),
    WITHDRAWN_PLAINTIFF(TransitionPhase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_PLAINTIFF_BCE),
    WITHDRAWN_RESPONDER(TransitionPhase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_RESPONDER_BCE),
    WITHDRAWN_SUB_INSTITUTION(TransitionPhase.CLOSURE, true, BlockchainEventType.WITHDRAWN_BY_SUB_INSTITUTION_BCE),
    WITHDRAWN_ARBITRATION(TransitionPhase.CLOSURE, true, BlockchainEventType.WITHDRAWN_DURING_ARBITRATION_BCE);

    private final TransitionPhase phase;
    private final boolean isBlockchainLoggable;
    private final BlockchainEventType blockchainEventType;

    private DisputeState(TransitionPhase phase, boolean isBlockchainLoggable, BlockchainEventType blockchainEventType) {
        this.phase = phase;
        this.isBlockchainLoggable = isBlockchainLoggable;
        this.blockchainEventType = blockchainEventType;
    }

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
                AWAITING_EVIDENCE_VERIFICATION,
                AWAITING_MANUAL_EVIDENCE_REVIEW,
                EVIDENCE_VERIFIED,
                EVIDENCE_REJECTED,
                AWAITING_PLAINTIFF_VERIFICATION,
                PLAINTIFF_VERIFIED,
                PLAINTIFF_DECLINED,
                AWAITING_RESPONDER_VERIFICATION,
                RESPONDER_VERIFIED,
                RESPONDER_DECLINED,
                ARBITRATION_PROCESSOR_AWAITING_RESPONSE,
                ARBITRATION_PROCESSOR_RESOLVED_PLAINTIFF_FAVOR,
                ARBITRATION_PROCESSOR_RESOLVED_RESPONDER_FAVOR,
                ARBITRATION_PROCESSOR_RESOLVED_AMBIGUOUS,
                ARBITRATION_AWAITING_MANUAL_REVIEW,
                ARBITRATION_MANUAL_RESOLVED_PLAINTIFF_FAVOR,
                ARBITRATION_MANUAL_RESOLVED_RESPONDER_FAVOR,
                ARBITRATION_MANUAL_RESOLVED_AMBIGUOUS,
                AUTHORITY_PLAINTIFF_ESCALATED,
                AUTHORITY_RESPONDER_ESCALATED,
                AUTHORITY_SYSTEM_ESCALATED,
                EXPIRED
        );
    }

    public static Set<DisputeState> getAllTerminalChildren() {
        return EnumSet.of(CLOSED);
    }

    public static Set<DisputeState> getAllWithdrawnChildren() {
        return EnumSet.of(
                WITHDRAWN_CUSTOMER,
                WITHDRAWN_PLAINTIFF,
                WITHDRAWN_RESPONDER,
                WITHDRAWN_ARBITRATION
        );
    }
}