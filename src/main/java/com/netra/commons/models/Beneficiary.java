package com.netra.commons.models;

import com.netra.commons.enums.AccountType;
import com.netra.commons.models.service.outlet.Merchant;
import jakarta.validation.constraints.*;

/**
 * Represents a beneficiary account that could belong to
 * either an individual or a merchant in a financial institution.
 */
public class Beneficiary {

    public enum BeneficiaryType {
        INDIVIDUAL,
        MERCHANT
    }

    @NotNull(message = "Beneficiary Account type is required")
    private Beneficiary.BeneficiaryType beneficiaryAccountType;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "\\d{6,20}", message = "Account number must be between 6 and 20 digits")
    private String accountNumber;

    @NotNull
    private FinancialInstitution financialInstitution;

    @NotBlank(message = "Account name is required")
    @Size(max = 100, message = "Account name must not exceed 100 characters")
    private String accountName;

    // Optional: for merchant accounts only
    private Merchant merchant;

    // Constructors
    public Beneficiary() {}

    // Getters & Setters
    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public FinancialInstitution getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }


    @Override
    public String toString() {
        return "Beneficiary{" +
                "accountType=" + accountType +
                ", accountNumber='" + accountNumber + '\'' +
                ", financialInstitution'" + financialInstitution + '\'' +
                ", accountName='" + accountName + '\'' +
                '}';
    }
}
