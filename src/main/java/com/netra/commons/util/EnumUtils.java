package com.netra.commons.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EnumUtils {

    private EnumUtils() {
        // Prevent instantiation
    }

    /**
     * Case-insensitive conversion from String to Enum constant.
     * Returns null if no match is found.
     */
    public static <T extends Enum<T>> T fromStringIgnoreCase(Class<T> enumClass, String value) {
        if (value == null || enumClass == null) {
            return null;
        }
        for (T constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value)) {
                return constant;
            }
        }
        return null;
    }

    /**
     * Exact match (case-sensitive) from String to Enum constant.
     * Returns null if no match is found.
     */
    public static <T extends Enum<T>> T fromStringExact(Class<T> enumClass, String value) {
        if (value == null || enumClass == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns all enum constant names as a List of Strings.
     */
    public static List<String> getNames(Class<? extends Enum<?>> enumClass) {
        if (enumClass == null) {
            return Collections.emptyList();
        }
        List<String> names = new ArrayList<>();
        for (Enum<?> constant : enumClass.getEnumConstants()) {
            names.add(constant.name());
        }
        return names;
    }

    /**
     * Checks if an enum contains a constant with the given name (case-insensitive).
     */
    public static <T extends Enum<T>> boolean containsNameIgnoreCase(Class<T> enumClass, String value) {
        return fromStringIgnoreCase(enumClass, value) != null;
    }

    /**
     * Checks if an enum contains a constant with the given name (case-sensitive).
     */
    public static <T extends Enum<T>> boolean containsNameExact(Class<T> enumClass, String value) {
        return fromStringExact(enumClass, value) != null;
    }

    /**
     * Safe conversion from String to Enum with a default fallback if no match.
     * Case-insensitive.
     */
    public static <T extends Enum<T>> T safeValueOf(Class<T> enumClass, String value, T defaultValue) {
        T result = fromStringIgnoreCase(enumClass, value);
        return result != null ? result : defaultValue;
    }
}
