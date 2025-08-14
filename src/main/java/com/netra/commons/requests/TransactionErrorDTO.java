package com.netra.commons.requests;

import com.netra.commons.enums.TransactionErrorType;
import com.netra.commons.models.TransactionType;
import lombok.Data;

@Data
public class TransactionErrorDTO {
    private TransactionErrorType errorType;
    private String errorCode;
    private String errorMessage;

}
