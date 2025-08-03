package com.netra.commons.models.rules;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.enums.DisputeState;
import com.netra.commons.enums.TransactionInstrument;
import com.netra.commons.models.BaseEntity;
import com.netra.commons.models.TransactionType;
import lombok.Data;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class DisputeRuleAggregator extends BaseEntity implements DisableAble {

    private Boolean disabled;
    private TransactionType transactionType;
    private Set<TransactionInstrument> transactionInstruments;
    // e.g., USSD, WEB, POS; allows some rules to be channel-sensitive




//    private Integer minDurationAfterTransactionBeforeDisputeCanBeCreated;
//    // To prevent premature dispute (e.g., before auto-reversal window closes)
//
//    private Integer maxDurationAfterTransactionWithinWhichDisputeMustBeInitiated;
//    // Regulatory requirement, e.g., “must raise a dispute within 30 days of txn”
//
//    private Integer maxDurationOfDisputeLifeCycleAfterConfirmedAsLegitDispute;
//    // Total SLA window before dispute must resolve (or escalate to final status)
//
//    private boolean requiresIssuerConfirmationBeforeLifecycleStart;
//    // Needed when only issuer can legitimize that a dispute is valid
//
//    private boolean autoEscalateIfUnresolvedWithinFinalWindow;
//    // Useful for compliance enforcement/escalation
//
//    private String regulatoryReferenceCode;
//    // Optional: maps to a known CBN or NIBSS policy section

    private List<DisputeTemporalRule> temporalRules;
    private List<DisputeRuleAggregator> timelineRules;
    private List<DisputeAccessRule> accessRules;
    private List<DisputeDecisionRule> decisionRules;


    private List<DisputeStateTransitionRule> disputeStateTransitionRules;
    // Dictates: From status A, within X minutes, must transition to B (can enforce via policy engine)


    private static Duration convertDisputeTimelineNumericDurationToJavaDuration(Integer durationInMinutes){
        return Duration.ofMinutes(durationInMinutes);
    }

    private static Map<DisputeState, Duration> convertMapTimelineRuleDisputeStateDuration(Map<DisputeState, Integer> allowedNextStatesWithMaxDurationAfterDisputeLifecycleCanBegin){
        return allowedNextStatesWithMaxDurationAfterDisputeLifecycleCanBegin.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> convertDisputeTimelineNumericDurationToJavaDuration(entry.getValue())
                ));
    }
}

/*

CREATE TABLE dispute_timeline_rule (
    id BIGreal PRIMARY KEY REFERENCES base_entity(id),
    disabled BOOLEAN NOT NULL DEFAULT false,
    transaction_type_id BIGINT NOT NULL REFERENCES transaction_type(id),
    min_duration_after_txn_minutes INTEGER,
    max_duration_after_txn_minutes INTEGER,
    max_duration_dispute_lifecycle_minutes INTEGER,
    requires_issuer_confirmation BOOLEAN NOT NULL DEFAULT false,
    auto_escalate_if_unresolved BOOLEAN NOT NULL DEFAULT false,
    regulatory_reference_code VARCHAR(100)
);

CREATE TABLE dispute_timeline_instruments (
    timeline_rule_id BIGINT REFERENCES dispute_timeline_rule(id),
    instrument transaction_instrument NOT NULL,
    id bigreal(primary)
);


CREATE TABLE dispute_timeline_rule (
    id BIGreal PRIMARY KEY REFERENCES,
    timeline_rule_id dispute_timeline_rule_id REFERENCES dispute_timeline_rule(id),
    state_transition_rule_id REFERENCES dispute_timeline_rule dispute_state_transition_rule(id)
    primary(timeline_rule_id, state_transition_rule_id)

    ):
 */
