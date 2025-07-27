package com.netra.commons.contracts;

public interface BlockchainAuditable extends Auditable{
    String getCurrentHash();
    String getPreviousHash();
    String getDigitalSignature();
}
