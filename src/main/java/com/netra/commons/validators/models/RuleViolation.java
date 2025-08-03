package com.netra.commons.validators.models;


public class RuleViolation {

    public enum Severity {
        ERROR,
        WARNING,
        INFO
    }

    private final String ruleName;
    private final String description;
    private final Severity severity;
    private final String category; // optional: e.g. "TRANSITION", "TIMELINE", "FRAUD"
    private final String referenceCode; // optional: regulatory or internal reference

    public RuleViolation(String ruleName, String description, Severity severity) {
        this(ruleName, description, severity, null, null);
    }

    public RuleViolation(String ruleName, String description, Severity severity, String category, String referenceCode) {
        this.ruleName = ruleName;
        this.description = description;
        this.severity = severity;
        this.category = category;
        this.referenceCode = referenceCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getDescription() {
        return description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getCategory() {
        return category;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    @Override
    public String toString() {
        return "PolicyViolation{" +
                "ruleName='" + ruleName + '\'' +
                ", description='" + description + '\'' +
                ", severity=" + severity +
                ", category=" + category +
                ", referenceCode=" + referenceCode +
                '}';
    }
}

