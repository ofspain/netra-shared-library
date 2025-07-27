package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.enums.DisputeState;
import com.netra.commons.enums.TransactionChannel;
import com.netra.commons.enums.TransactionInstrument;
import lombok.Data;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class DisputeTimelineRule extends BaseEntity implements DisableAble {

    private Boolean disabled;
    private TransactionType transactionType;
    private Set<TransactionInstrument> transactionInstruments;
    // e.g., USSD, WEB, POS; allows some rules to be channel-sensitive



    private Map<DisputeState, Integer> allowedNextStatesWithMaxDurationAfterDisputeLifecycleCanBegin;
    // Dictates: From status A, within X hours, must transition to B (can enforce via policy engine)

    private Integer minDurationAfterTransactionBeforeDisputeCanBeCreated;
    // To prevent premature dispute (e.g., before auto-reversal window closes)

    private Integer maxDurationAfterTransactionWithinWhichDisputeMustBeInitiated;
    // Regulatory requirement, e.g., “must raise a dispute within 30 days of txn”

    private Integer maxDurationOfDisputeLifeCycleAfterConfirmedAsLegitDispute;
    // Total SLA window before dispute must resolve (or escalate to final status)

    private boolean requiresIssuerConfirmationBeforeLifecycleStart;
    // Needed when only issuer can legitimize that a dispute is valid

    private boolean autoEscalateIfUnresolvedWithinFinalWindow;
    // Useful for compliance enforcement/escalation

    private String regulatoryReferenceCode;
    // Optional: maps to a known CBN or NIBSS policy section


    private static Duration convertDisputeTimelineNumericDurationToJavaDuration(Integer durationInHours){
        return Duration.ofDays(durationInHours);
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
