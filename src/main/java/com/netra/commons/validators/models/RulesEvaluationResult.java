package com.netra.commons.validators.models;


import java.util.List;
import java.util.Objects;

public class RulesEvaluationResult {

    private final boolean compliant;
    private final List<RuleViolation> violations;

    public RulesEvaluationResult(boolean compliant, List<RuleViolation> violations) {
        this.compliant = compliant;
        this.violations = violations;
    }

    public boolean isCompliant() {
        return compliant;
    }

    public List<RuleViolation> getViolations() {
        return violations;
    }

    public boolean hasErrorsOnly() {
        return violations.stream().allMatch(v -> v.getSeverity() == RuleViolation.Severity.ERROR);
    }

    public boolean hasWarnings() {
        return violations.stream().anyMatch(v -> v.getSeverity() == RuleViolation.Severity.WARNING);
    }

    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    @Override
    public String toString() {
        return "PolicyEvaluationResult{" +
                "compliant=" + compliant +
                ", violations=" + violations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RulesEvaluationResult)) return false;
        RulesEvaluationResult that = (RulesEvaluationResult) o;
        return compliant == that.compliant && Objects.equals(violations, that.violations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compliant, violations);
    }
}
