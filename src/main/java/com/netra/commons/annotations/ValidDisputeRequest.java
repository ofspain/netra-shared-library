package com.netra.commons.annotations;

import com.netra.commons.validators.DisputeRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DisputeRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDisputeRequest {
    String message() default "Dispute request is invalid based on disputant type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

