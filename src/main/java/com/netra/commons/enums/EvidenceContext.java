package com.netra.commons.enums;

public enum EvidenceContext {
    /* ---------------- EVIDENCE VERIFICATION ---------------- */
    CUSTOMER_PROOF_OF_TRANSACTION,             // Customer submits receipt, SMS, etc.
    MERCHANT_PROOF_OF_DELIVERY,                // Merchant provides delivery confirmation
    BANK_PROOF_OF_CREDIT_OR_DEBIT,             // FI provides ledger evidence
    FRAUD_DOCUMENTATION,                       // Fraud analysis or blacklisting data
    CUSTOMER_ID_VERIFICATION,                  // To verify ownership of account/card
    CORRESPONDENCE_LOGS,                       // Communication or chat/email proof

    /* ---------------- ISSUER / ACQUIRER VERIFICATION ---------------- */
    ISSUER_RESPONSE_EVIDENCE,                  // Issuer's reasoned response or logs
    ACQUIRER_RESPONSE_EVIDENCE,                // Acquirer’s proof of processing
    PROCESSOR_LOGS,                            // Processor transaction trace evidence
    NETWORK_SETTLEMENT_REPORT,                 // Visa/Mastercard/NIBSS-like reports

    /* ---------------- ARBITRATION ---------------- */
    ARBITRATION_SUMMARY_DOCUMENT,              // Case summary compiled by arbiter
    ARBITRATION_PANEL_REVIEW_NOTE,             // Internal decision notes
    ARBITRATION_ESCALATION_EVIDENCE,           // Filed when party escalates to manual
    ARBITRATION_MANUAL_REVIEW_EVIDENCE,        // Evidence used in manual recheck
    ARBITRATION_RULING_DOCUMENT,               // Final ruling / decision notice

    /* ---------------- AUTHORITY ---------------- */
    AUTHORITY_ESCALATION_FORM,                 // Regulatory or oversight appeal
    AUTHORITY_RULING_DECREE,                   // Authority resolution (final decision)
    AUTHORITY_COMMUNICATION_LOG,               // Email, correspondence, hearing notice

    /* ---------------- WITHDRAWAL FLOW ---------------- */
    WITHDRAWAL_REQUEST_CUSTOMER,               // Customer voluntarily withdraws claim
    WITHDRAWAL_REQUEST_ISSUER,                 // Issuer withdraws on behalf of cardholder
    WITHDRAWAL_REQUEST_ACQUIRER,               // Acquirer retracts dispute
    WITHDRAWAL_REQUEST_ARBITRATION,            // Withdrawn during arbitration
    WITHDRAWAL_REQUEST_AUTHORITY,              // Authority withdrawal or override
    WITHDRAWAL_APPROVAL_DOCUMENT,              // Approval confirming withdrawal accepted
    WITHDRAWAL_REASON_NOTE,                    // Explanation / justification document

    /* ---------------- SYSTEM / MISC ---------------- */
    SYSTEM_LOG_SNAPSHOT,                       // System-level event trace
    ADMIN_OVERRIDE_NOTE,                       // Admin/moderator manual input
    SUPPORTING_CORRESPONDENCE,                 // Any general support evidence
    REPRESENTMENT_PROOF,                       // For post-resolution representment flow
    ATTACHMENT_REFERENCE_DOCUMENT              // External doc (e.g., blockchain anchor ref)

}
