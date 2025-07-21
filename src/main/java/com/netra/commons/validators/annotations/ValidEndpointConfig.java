package com.netra.commons.validators.annotations;

import com.netra.commons.validators.DisputeRequestValidator;
import com.netra.commons.validators.EndpointConfigValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EndpointConfigValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEndpointConfig {
    String message() default "Endpoint Configuration fail validation check";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

