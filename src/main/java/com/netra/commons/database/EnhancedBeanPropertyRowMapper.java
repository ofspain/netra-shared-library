package com.netra.commons.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EnhancedBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = createDefaultObjectMapper();
    private static final Map<Class<?>, TypeReference<?>> TYPE_REFERENCE_CACHE = new ConcurrentHashMap<>();
    private static final Set<Class<?>> SIMPLE_TYPES = createSimpleTypesSet();
    private static final Map<String, DateTimeFormatter> DATE_FORMATTERS = createDateFormatters();

    private final ObjectMapper objectMapper;
    private final boolean failOnUnknownProperties;
    private final boolean logMappingWarnings;
    private final String nullValueReplacement;

    // Constructor with default settings
    public EnhancedBeanPropertyRowMapper() {
        this(DEFAULT_OBJECT_MAPPER, false, true, null);
    }

    // Constructor with custom ObjectMapper
    public EnhancedBeanPropertyRowMapper(ObjectMapper objectMapper) {
        this(objectMapper, false, true, null);
    }

    // Full constructor
    public EnhancedBeanPropertyRowMapper(ObjectMapper objectMapper,
                                         boolean failOnUnknownProperties,
                                         boolean logMappingWarnings,
                                         String nullValueReplacement) {
        this.objectMapper = objectMapper != null ? objectMapper : DEFAULT_OBJECT_MAPPER;
        this.failOnUnknownProperties = failOnUnknownProperties;
        this.logMappingWarnings = logMappingWarnings;
        this.nullValueReplacement = nullValueReplacement;
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        Class<?> targetType = pd.getPropertyType();
        String columnLabel = getColumnLabel(rs, index);

        // Handle null values first
        Object rawValue = rs.getObject(index);
        if (rawValue == null || rs.wasNull()) {
            return handleNullValue(targetType, columnLabel, pd.getName());
        }

        try {
            Object mappedValue = mapValue(rs, index, targetType, columnLabel, rawValue, pd);
            if (mappedValue != null) {
                return mappedValue;
            }
        } catch (Exception e) {
            if (logMappingWarnings) {
                log.warn("Failed to map column '{}' (type: {}) to property '{}' (type: {}): {}",
                        columnLabel, rawValue.getClass().getSimpleName(),
                        pd.getName(), targetType.getSimpleName(), e.getMessage());
            }

            if (failOnUnknownProperties) {
                throw new SQLException("Failed to map column: " + columnLabel, e);
            }
        }

        // Fall back to default Spring mapping
        return super.getColumnValue(rs, index, pd);
    }

    private Object mapValue(ResultSet rs, int index, Class<?> targetType,
                            String columnLabel, Object rawValue, PropertyDescriptor pd) throws SQLException {

        // 1. Handle primitive and wrapper types
        if (isPrimitiveOrWrapper(targetType)) {
            return mapPrimitiveType(rs, index, targetType, rawValue);
        }

        // 2. Handle String
        if (String.class.equals(targetType)) {
            return convertToString(rawValue);
        }

        // 3. Handle temporal types
        if (isTemporalType(targetType)) {
            return mapTemporalType(rs, index, targetType, rawValue);
        }

        // 4. Handle BigDecimal and BigInteger
        if (isBigNumberType(targetType)) {
            return mapBigNumberType(targetType, rawValue);
        }

        // 5. Handle Enums
        if (Enum.class.isAssignableFrom(targetType)) {
            return mapEnumType(targetType, rawValue);
        }

        // 6. Handle Arrays
        if (targetType.isArray()) {
            return mapArrayType(targetType, rawValue);
        }

        // 7. Handle Collections (List, Set, Queue, etc.)
        if (Collection.class.isAssignableFrom(targetType)) {
            return mapCollectionType(targetType, rawValue, pd);
        }

        // 8. Handle Maps
        if (Map.class.isAssignableFrom(targetType)) {
            return mapMapType(targetType, rawValue, pd);
        }

        // 9. Handle Optional
        if (Optional.class.equals(targetType)) {
            return mapOptionalType(rawValue, pd);
        }

        // 10. Handle UUID
        if (UUID.class.equals(targetType)) {
            return mapUUIDType(rawValue);
        }

        // 11. Handle complex objects (JSON/JSONB)
        if (!isSimpleType(targetType)) {
            return mapComplexType(targetType, rawValue);
        }

        return null;
    }

    private Object mapPrimitiveType(ResultSet rs, int index, Class<?> targetType, Object rawValue) throws SQLException {
        if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) {
            return rs.getBoolean(index);
        } else if (targetType.equals(byte.class) || targetType.equals(Byte.class)) {
            return rs.getByte(index);
        } else if (targetType.equals(short.class) || targetType.equals(Short.class)) {
            return rs.getShort(index);
        } else if (targetType.equals(int.class) || targetType.equals(Integer.class)) {
            return rs.getInt(index);
        } else if (targetType.equals(long.class) || targetType.equals(Long.class)) {
            return rs.getLong(index);
        } else if (targetType.equals(float.class) || targetType.equals(Float.class)) {
            return rs.getFloat(index);
        } else if (targetType.equals(double.class) || targetType.equals(Double.class)) {
            return rs.getDouble(index);
        }
        return null;
    }

    private Object mapTemporalType(ResultSet rs, int index, Class<?> targetType, Object rawValue) throws SQLException {
        if (LocalDateTime.class.equals(targetType)) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        } else if (LocalDate.class.equals(targetType)) {
            Date date = rs.getDate(index);
            return date != null ? date.toLocalDate() : null;
        } else if (LocalTime.class.equals(targetType)) {
            Time time = rs.getTime(index);
            return time != null ? time.toLocalTime() : null;
        } else if (Instant.class.equals(targetType)) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp != null ? timestamp.toInstant() : null;
        } else if (ZonedDateTime.class.equals(targetType)) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp != null ? ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null;
        } else if (OffsetDateTime.class.equals(targetType)) {
            Timestamp timestamp = rs.getTimestamp(index);
            return timestamp != null ? OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null;
        }

        // Try parsing from string if it's a string representation
        if (rawValue instanceof String) {
            return parseTemporalFromString((String) rawValue, targetType);
        }

        return null;
    }

    private Object parseTemporalFromString(String value, Class<?> targetType) {
        for (Map.Entry<String, DateTimeFormatter> entry : DATE_FORMATTERS.entrySet()) {
            try {
                if (LocalDateTime.class.equals(targetType)) {
                    return LocalDateTime.parse(value, entry.getValue());
                } else if (LocalDate.class.equals(targetType)) {
                    return LocalDate.parse(value, entry.getValue());
                } else if (LocalTime.class.equals(targetType)) {
                    return LocalTime.parse(value, entry.getValue());
                }
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        return null;
    }

    private Object mapBigNumberType(Class<?> targetType, Object rawValue) {
        String stringValue = convertToString(rawValue);
        if (!StringUtils.hasText(stringValue)) {
            return null;
        }

        try {
            if (BigDecimal.class.equals(targetType)) {
                return new BigDecimal(stringValue);
            } else if (BigInteger.class.equals(targetType)) {
                return new BigInteger(stringValue);
            }
        } catch (NumberFormatException e) {
            if (logMappingWarnings) {
                log.warn("Failed to parse {} as {}: {}", stringValue, targetType.getSimpleName(), e.getMessage());
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object mapEnumType(Class<?> targetType, Object rawValue) {
        String stringValue = convertToString(rawValue);
        if (!StringUtils.hasText(stringValue)) {
            return null;
        }

        try {
            // Try exact match first
            return Enum.valueOf((Class<Enum>) targetType, stringValue);
        } catch (IllegalArgumentException e1) {
            try {
                // Try uppercase
                return Enum.valueOf((Class<Enum>) targetType, stringValue.toUpperCase());
            } catch (IllegalArgumentException e2) {
                try {
                    // Try lowercase
                    return Enum.valueOf((Class<Enum>) targetType, stringValue.toLowerCase());
                } catch (IllegalArgumentException e3) {
                    if (logMappingWarnings) {
                        log.warn("No enum constant {} in {}", stringValue, targetType.getSimpleName());
                    }
                }
            }
        }
        return null;
    }

    private Object mapArrayType(Class<?> targetType, Object rawValue) {
        if (rawValue instanceof Array) {
            try {
                Array sqlArray = (Array) rawValue;
                Object[] array = (Object[]) sqlArray.getArray();
                Class<?> componentType = targetType.getComponentType();

                if (componentType.equals(String.class)) {
                    return Arrays.copyOf(array, array.length, String[].class);
                } else if (componentType.equals(Integer.class) || componentType.equals(int.class)) {
                    return Arrays.stream(array).mapToInt(o -> Integer.parseInt(o.toString())).toArray();
                } else if (componentType.equals(Long.class) || componentType.equals(long.class)) {
                    return Arrays.stream(array).mapToLong(o -> Long.parseLong(o.toString())).toArray();
                }
                // Add more array type conversions as needed

            } catch (SQLException e) {
                if (logMappingWarnings) {
                    log.warn("Failed to convert SQL array to Java array: {}", e.getMessage());
                }
            }
        }

        // Try JSON array parsing
        String stringValue = convertToString(rawValue);
        if (isLikelyJsonArray(stringValue)) {
            try {
                return objectMapper.readValue(stringValue, targetType);
            } catch (JsonProcessingException e) {
                if (logMappingWarnings) {
                    log.warn("Failed to parse JSON array: {}", e.getMessage());
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Object mapCollectionType(Class<?> targetType, Object rawValue, PropertyDescriptor pd) {
        String stringValue = convertToString(rawValue);

        // Handle comma-delimited strings for simple collections
        if (StringUtils.hasText(stringValue) && !isLikelyJson(stringValue)) {
            Set<String> stringSet = StringUtils.commaDelimitedListToSet(stringValue);

            if (Set.class.isAssignableFrom(targetType)) {
                return stringSet;
            } else if (List.class.isAssignableFrom(targetType)) {
                return new ArrayList<>(stringSet);
            } else if (Collection.class.isAssignableFrom(targetType)) {
                return stringSet;
            }
        }

        // Handle JSON arrays
        if (isLikelyJsonArray(stringValue)) {
            try {
                Type genericType = pd.getReadMethod().getGenericReturnType();
                JavaType javaType = objectMapper.getTypeFactory().constructType(genericType);
                return objectMapper.readValue(stringValue, javaType);
            } catch (JsonProcessingException e) {
                if (logMappingWarnings) {
                    log.warn("Failed to parse JSON collection: {}", e.getMessage());
                }
            }
        }

        return null;
    }

    private Object mapMapType(Class<?> targetType, Object rawValue, PropertyDescriptor pd) {
        String stringValue = convertToString(rawValue);

        if (isLikelyJson(stringValue)) {
            try {
                Type genericType = pd.getReadMethod().getGenericReturnType();
                JavaType javaType = objectMapper.getTypeFactory().constructType(genericType);
                return objectMapper.readValue(stringValue, javaType);
            } catch (JsonProcessingException e) {
                if (logMappingWarnings) {
                    log.warn("Failed to parse JSON map: {}", e.getMessage());
                }
            }
        }

        return new HashMap<>();
    }

    private Object mapOptionalType(Object rawValue, PropertyDescriptor pd) {
        if (rawValue == null) {
            return Optional.empty();
        }

        // Get the generic type of Optional
        Type genericType = pd.getReadMethod().getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericType;
            Type[] actualTypes = paramType.getActualTypeArguments();
            if (actualTypes.length > 0) {
                Class<?> optionalType = (Class<?>) actualTypes[0];
                // Recursively map the inner type
                // This is simplified - you might need more complex logic here
                if (String.class.equals(optionalType)) {
                    return Optional.of(convertToString(rawValue));
                }
            }
        }

        return Optional.of(rawValue);
    }

    private Object mapUUIDType(Object rawValue) {
        String stringValue = convertToString(rawValue);
        if (StringUtils.hasText(stringValue)) {
            try {
                return UUID.fromString(stringValue);
            } catch (IllegalArgumentException e) {
                if (logMappingWarnings) {
                    log.warn("Failed to parse UUID from: {}", stringValue);
                }
            }
        }
        return null;
    }

    private Object mapComplexType(Class<?> targetType, Object rawValue) {
        String stringValue = convertToString(rawValue);

        if (isLikelyJson(stringValue)) {
            try {
                return objectMapper.readValue(stringValue, targetType);
            } catch (JsonProcessingException e) {
                if (logMappingWarnings) {
                    log.warn("Failed to deserialize JSON to {}: {}", targetType.getSimpleName(), e.getMessage());
                }
            }
        }

        return null;
    }

    private Object handleNullValue(Class<?> targetType, String columnLabel, String propertyName) {
        if (nullValueReplacement != null && String.class.equals(targetType)) {
            return nullValueReplacement;
        }

        // Return appropriate defaults for primitives
        if (targetType.isPrimitive()) {
            if (targetType.equals(boolean.class)) return false;
            if (targetType.equals(byte.class)) return (byte) 0;
            if (targetType.equals(short.class)) return (short) 0;
            if (targetType.equals(int.class)) return 0;
            if (targetType.equals(long.class)) return 0L;
            if (targetType.equals(float.class)) return 0.0f;
            if (targetType.equals(double.class)) return 0.0;
            if (targetType.equals(char.class)) return '\0';
        }

        // Return empty collections for collection types
        if (Collection.class.isAssignableFrom(targetType)) {
            if (List.class.isAssignableFrom(targetType)) return new ArrayList<>();
            if (Set.class.isAssignableFrom(targetType)) return new HashSet<>();
            return new ArrayList<>();
        }

        if (Map.class.isAssignableFrom(targetType)) {
            return new HashMap<>();
        }

        if (Optional.class.equals(targetType)) {
            return Optional.empty();
        }

        return null;
    }

    // Utility methods
    private String getColumnLabel(ResultSet rs, int index) throws SQLException {
        try {
            return rs.getMetaData().getColumnLabel(index);
        } catch (SQLException e) {
            return "column_" + index;
        }
    }

    private String convertToString(Object value) {
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        if (value instanceof Clob) {
            try {
                Clob clob = (Clob) value;
                return clob.getSubString(1, (int) clob.length());
            } catch (SQLException e) {
                if (logMappingWarnings) {
                    log.warn("Failed to read CLOB: {}", e.getMessage());
                }
                return null;
            }
        }
        return value.toString();
    }

    private boolean isLikelyJson(String value) {
        if (!StringUtils.hasText(value)) return false;
        String trimmed = value.trim();
        return (trimmed.startsWith("{") && trimmed.endsWith("}")) ||
                (trimmed.startsWith("[") && trimmed.endsWith("]"));
    }

    private boolean isLikelyJsonArray(String value) {
        if (!StringUtils.hasText(value)) return false;
        String trimmed = value.trim();
        return trimmed.startsWith("[") && trimmed.endsWith("]");
    }

    private boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPES.contains(clazz) || clazz.isPrimitive();
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class.equals(clazz) ||
                Character.class.equals(clazz);
    }

    private boolean isTemporalType(Class<?> clazz) {
        return LocalDateTime.class.equals(clazz) ||
                LocalDate.class.equals(clazz) ||
                LocalTime.class.equals(clazz) ||
                Instant.class.equals(clazz) ||
                ZonedDateTime.class.equals(clazz) ||
                OffsetDateTime.class.equals(clazz) ||
                Date.class.isAssignableFrom(clazz);
    }

    private boolean isBigNumberType(Class<?> clazz) {
        return BigDecimal.class.equals(clazz) || BigInteger.class.equals(clazz);
    }

    // Static factory methods
    public static <T> EnhancedBeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        EnhancedBeanPropertyRowMapper<T> mapper = new EnhancedBeanPropertyRowMapper<>();
        mapper.setMappedClass(mappedClass);
        mapper.setCheckFullyPopulated(false);
        mapper.setPrimitivesDefaultedForNullValue(true);
        return mapper;
    }

    public static <T> EnhancedBeanPropertyRowMapper<T> newInstance(Class<T> mappedClass,
                                                                   ObjectMapper objectMapper) {
        EnhancedBeanPropertyRowMapper<T> mapper = new EnhancedBeanPropertyRowMapper<>(objectMapper);
        mapper.setMappedClass(mappedClass);
        mapper.setCheckFullyPopulated(false);
        mapper.setPrimitivesDefaultedForNullValue(true);
        return mapper;
    }

    public static <T> EnhancedBeanPropertyRowMapper<T> newStrictInstance(Class<T> mappedClass) {
        EnhancedBeanPropertyRowMapper<T> mapper = new EnhancedBeanPropertyRowMapper<>(
                DEFAULT_OBJECT_MAPPER, true, true, null);
        mapper.setMappedClass(mappedClass);
        mapper.setCheckFullyPopulated(true);
        mapper.setPrimitivesDefaultedForNullValue(false);
        return mapper;
    }

    // Static initialization methods
    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        return mapper;
    }

    private static Set<Class<?>> createSimpleTypesSet() {
        Set<Class<?>> types = new HashSet<>();

        // Primitives and wrappers
        types.add(boolean.class);
        types.add(Boolean.class);
        types.add(byte.class);
        types.add(Byte.class);
        types.add(short.class);
        types.add(Short.class);
        types.add(int.class);
        types.add(Integer.class);
        types.add(long.class);
        types.add(Long.class);
        types.add(float.class);
        types.add(Float.class);
        types.add(double.class);
        types.add(Double.class);
        types.add(char.class);
        types.add(Character.class);

        // Common types
        types.add(String.class);
        types.add(Date.class);
        types.add(java.sql.Date.class);
        types.add(Time.class);
        types.add(Timestamp.class);
        types.add(BigDecimal.class);
        types.add(BigInteger.class);
        types.add(UUID.class);

        // Java 8 time types
        types.add(LocalDate.class);
        types.add(LocalTime.class);
        types.add(LocalDateTime.class);
        types.add(Instant.class);
        types.add(ZonedDateTime.class);
        types.add(OffsetDateTime.class);

        return Collections.unmodifiableSet(types);
    }

    private static Map<String, DateTimeFormatter> createDateFormatters() {
        Map<String, DateTimeFormatter> formatters = new LinkedHashMap<>();

        // ISO formats (try these first)
        formatters.put("ISO_LOCAL_DATE_TIME", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        formatters.put("ISO_LOCAL_DATE", DateTimeFormatter.ISO_LOCAL_DATE);
        formatters.put("ISO_LOCAL_TIME", DateTimeFormatter.ISO_LOCAL_TIME);
        formatters.put("ISO_INSTANT", DateTimeFormatter.ISO_INSTANT);

        // Common database formats
        formatters.put("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        formatters.put("yyyy-MM-dd HH:mm:ss.SSS", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        formatters.put("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formatters.put("HH:mm:ss", DateTimeFormatter.ofPattern("HH:mm:ss"));
        formatters.put("HH:mm:ss.SSS", DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        // Additional common formats
        formatters.put("dd/MM/yyyy", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        formatters.put("MM/dd/yyyy", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        formatters.put("dd-MM-yyyy", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        formatters.put("MM-dd-yyyy", DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        return Collections.unmodifiableMap(formatters);
    }
}