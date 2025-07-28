package com.netra.commons.exceptions;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.NestedRuntimeException;

import java.util.Locale;


public class ExceptionUtil {

    public static String generateErrorLogKey(String prefix) {
        return String.format("%s-%s", prefix, RandomStringUtils.randomAlphanumeric(6).toUpperCase(Locale.ROOT));
    }

    public static Throwable getMostSpecificCause(Exception e) {
        if (e instanceof NestedRuntimeException) {
            return ((NestedRuntimeException) e).getMostSpecificCause();
        } else {
            Throwable rootCause = getRootCause(e);
            return rootCause != null ? rootCause : e;
        }
    }

    private static Throwable getRootCause(Exception e) {
        Throwable rootCause = null;
        for(Throwable cause = e.getCause(); cause != null && cause != rootCause; cause = cause.getCause()) {
            rootCause = cause;
        }
        return rootCause;
    }

    public static boolean contains(Exception e, Class<? extends Exception> eClass) {
        if (e == null || eClass == null) {
            return false;
        }
        if (eClass.isInstance(e)) {
            return true;
        }
        Throwable cause = e.getCause();
        if (cause == e) {
            return false;
        }
        if (cause instanceof NestedRuntimeException) {
            return ((NestedRuntimeException) cause).contains(eClass);
        }
        else {
            while (cause != null) {
                if (eClass.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }

    public static Throwable get(Exception e, Class<? extends Exception> eClass) {
        if (e == null || eClass == null) {
            return null;
        }
        if (eClass.isInstance(e)) {
            return e;
        }
        Throwable cause = e.getCause();
        if (cause == e) {
            return e;
        }
        while (cause != null) {
            if (eClass.isInstance(cause)) {
                return cause;
            }
            if (cause.getCause() == cause) {
                break;
            }
            cause = cause.getCause();
        }
        return null;
    }
}
