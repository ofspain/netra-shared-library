package com.netra.commons.validators;

import com.netra.commons.annotations.ValidDisputeRequest;
import com.netra.commons.enums.DisputantType;
import com.netra.commons.models.Evidence;
import com.netra.commons.requests.CreateDisputeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class DisputeRequestValidator implements ConstraintValidator<ValidDisputeRequest, CreateDisputeRequest> {

    @Override
    public boolean isValid(CreateDisputeRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getInitiator() == null) {
            return false;
        }

        DisputantType type = request.getInitiator().getDisputantType();

        // Evidence required for CUSTOMER but not for ISSUER_USER
        if (type.equals(DisputantType.CUSTOMERUSER)) {
            List<Evidence> evidences = request.getEvidences();
            if (evidences == null || evidences.isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Evidence is required for CUSTOMER disputes.")
                        .addPropertyNode("evidences")
                        .addConstraintViolation();
                return false;
            }
        }

        // Add any additional conditional logic here...

        return true;
    }
}
