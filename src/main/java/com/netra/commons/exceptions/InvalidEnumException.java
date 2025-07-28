package com.netra.commons.exceptions;

import lombok.Getter;

import java.util.Arrays;

@Getter
public final class InvalidEnumException extends RuntimeException implements BaseException {

    private final Object rejected;
    private final Object[] expected;
    private final Class<? extends Enum<?>> enumClass;

    public InvalidEnumException(Class<? extends Enum<?>> enumClass, Object rejected, Object[] expected) {
        this.enumClass = enumClass;
        this.rejected = rejected;
        this.expected = expected;
    }

    @Override
    public String getMessage() {
        return String.format("'%s' is not a valid %s. Expected: [%s]",
                rejected,
                enumClass.getSimpleName(),
                String.join(", ", Arrays.stream(expected)
                        .map(Object::toString)
                        .toList()));
    }

    public String getAppErrorCode(){
        return "ENUM_ERR";
    }
}

