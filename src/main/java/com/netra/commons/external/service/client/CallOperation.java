package com.netra.commons.external.service.client;


/**
 * Enumeration representing different types of store/data operations
 * used for logging and monitoring purposes in REST client calls.
 *
 * This enum helps categorize API operations for better observability,
 * metrics collection, and audit trails.
 */
public enum CallOperation {

    // CRUD Operations
    /**
     * Create/Insert operation - Adding new data
     * Examples: POST /users, POST /orders
     */
    CREATE("CREATE", "Creating new resource"),

    /**
     * Read/Select operation - Retrieving existing data
     * Examples: GET /users/{id}, GET /orders?status=pending
     */
    READ("READ", "Reading existing resource"),

    /**
     * Update operation - Modifying existing data
     * Examples: PUT /users/{id}, PATCH /orders/{id}
     */
    UPDATE("UPDATE", "Updating existing resource"),

    /**
     * Delete operation - Removing existing data
     * Examples: DELETE /users/{id}, DELETE /orders/{id}
     */
    DELETE("DELETE", "Deleting existing resource"),

    // Business Operations
    /**
     * Search/Query operation - Complex data retrieval
     * Examples: POST /users/search, GET /orders/filter
     */
    SEARCH("SEARCH", "Searching resources with criteria"),

    /**
     * Authentication operation
     * Examples: POST /auth/login, POST /oauth/token
     */
    AUTHENTICATE("AUTHENTICATE", "User authentication"),

    /**
     * Authorization/Permission check operation
     * Examples: GET /auth/permissions, POST /auth/validate
     */
    AUTHORIZE("AUTHORIZE", "Authorization validation"),

    /**
     * Validation operation - Data or business rule validation
     * Examples: POST /validate/account, POST /verify/transaction
     */
    VALIDATE("VALIDATE", "Data validation"),

    /**
     * Processing operation - Business logic execution
     * Examples: POST /process/payment, POST /execute/workflow
     */
    PROCESS("PROCESS", "Processing business logic"),

    /**
     * Notification operation - Sending notifications
     * Examples: POST /notify/email, POST /send/sms
     */
    NOTIFY("NOTIFY", "Sending notification"),

    /**
     * Export operation - Data export/download
     * Examples: GET /export/report, POST /download/data
     */
    EXPORT("EXPORT", "Exporting data"),

    /**
     * Import operation - Data import/upload
     * Examples: POST /import/users, PUT /upload/file
     */
    IMPORT("IMPORT", "Importing data"),

    /**
     * Synchronization operation - Data sync between systems
     * Examples: POST /sync/users, PUT /synchronize/inventory
     */
    SYNC("SYNC", "Synchronizing data"),

    /**
     * Health check or monitoring operation
     * Examples: GET /health, GET /status
     */
    HEALTH_CHECK("HEALTH_CHECK", "Health check operation"),

    /**
     * Configuration operation - System configuration
     * Examples: GET /config, PUT /settings
     */
    CONFIG("CONFIG", "Configuration operation"),

    /**
     * Audit operation - Audit trail or logging
     * Examples: POST /audit/log, GET /audit/trail
     */
    AUDIT("AUDIT", "Audit operation"),

    /**
     * Batch operation - Processing multiple items
     * Examples: POST /batch/process, PUT /bulk/update
     */
    BATCH("BATCH", "Batch processing operation"),

    /**
     * Transaction operation - Financial or business transaction
     * Examples: POST /transaction/transfer, PUT /payment/process
     */
    TRANSACTION("TRANSACTION", "Transaction processing"),

    /**
     * Unknown or unspecified operation
     */
    UNKNOWN("UNKNOWN", "Unknown operation type");

    private final String code;
    private final String description;

    /**
     * Constructor for StoreOperation enum
     *
     * @param code Short code representation of the operation
     * @param description Human-readable description of the operation
     */
    CallOperation(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Gets the operation code
     *
     * @return String code representing the operation
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the operation description
     *
     @return String description of the operation
     */
    public String getDescription() {
        return description;
    }

    /**
     * Determines the appropriate StoreOperation based on HTTP method and URL pattern
     * This is a utility method for automatic operation detection
     *
     * @param httpMethod HTTP method (GET, POST, PUT, DELETE, etc.)
     * @param url The request URL
     * @return Most appropriate StoreOperation
     */
    public static CallOperation fromHttpMethodAndUrl(String httpMethod, String url) {
        if (httpMethod == null || url == null) {
            return UNKNOWN;
        }

        String method = httpMethod.toUpperCase();
        String lowerUrl = url.toLowerCase();

        // Authentication/Authorization patterns
        if (lowerUrl.contains("/auth") || lowerUrl.contains("/login") || lowerUrl.contains("/token")) {
            return AUTHENTICATE;
        }
        if (lowerUrl.contains("/authorize") || lowerUrl.contains("/permission")) {
            return AUTHORIZE;
        }

        // Health and monitoring
        if (lowerUrl.contains("/health") || lowerUrl.contains("/status") || lowerUrl.contains("/ping")) {
            return HEALTH_CHECK;
        }

        // Business operations
        if (lowerUrl.contains("/search") || lowerUrl.contains("/query") || lowerUrl.contains("/filter")) {
            return SEARCH;
        }
        if (lowerUrl.contains("/validate") || lowerUrl.contains("/verify")) {
            return VALIDATE;
        }
        if (lowerUrl.contains("/process") || lowerUrl.contains("/execute")) {
            return PROCESS;
        }
        if (lowerUrl.contains("/notify") || lowerUrl.contains("/send")) {
            return NOTIFY;
        }
        if (lowerUrl.contains("/export") || lowerUrl.contains("/download")) {
            return EXPORT;
        }
        if (lowerUrl.contains("/import") || lowerUrl.contains("/upload")) {
            return IMPORT;
        }
        if (lowerUrl.contains("/sync") || lowerUrl.contains("/synchronize")) {
            return SYNC;
        }
        if (lowerUrl.contains("/config") || lowerUrl.contains("/settings")) {
            return CONFIG;
        }
        if (lowerUrl.contains("/audit") || lowerUrl.contains("/log")) {
            return AUDIT;
        }
        if (lowerUrl.contains("/batch") || lowerUrl.contains("/bulk")) {
            return BATCH;
        }
        if (lowerUrl.contains("/transaction") || lowerUrl.contains("/payment") || lowerUrl.contains("/transfer")) {
            return TRANSACTION;
        }

        // Default CRUD operations based on HTTP method
        switch (method) {
            case "GET":
                return READ;
            case "POST":
                return CREATE;
            case "PUT":
            case "PATCH":
                return UPDATE;
            case "DELETE":
                return DELETE;
            default:
                return UNKNOWN;
        }
    }

    /**
     * Checks if this operation is a read-only operation
     *
     * @return true if the operation is read-only (doesn't modify data)
     */
    public boolean isReadOnly() {
        return this == READ || this == SEARCH || this == HEALTH_CHECK ||
                this == EXPORT || this == AUDIT;
    }

    /**
     * Checks if this operation is a write operation
     *
     * @return true if the operation modifies data
     */
    public boolean isWriteOperation() {
        return this == CREATE || this == UPDATE || this == DELETE ||
                this == IMPORT || this == PROCESS || this == TRANSACTION;
    }

    /**
     * Checks if this operation is security-related
     *
     * @return true if the operation is related to security
     */
    public boolean isSecurityOperation() {
        return this == AUTHENTICATE || this == AUTHORIZE || this == VALIDATE;
    }

    /**
     * Gets the category of this operation
     *
     * @return OperationCategory enum value
     */
    public OperationCategory getCategory() {
        if (isSecurityOperation()) {
            return OperationCategory.SECURITY;
        }
        if (isReadOnly()) {
            return OperationCategory.READ;
        }
        if (isWriteOperation()) {
            return OperationCategory.WRITE;
        }
        if (this == NOTIFY || this == SYNC || this == BATCH) {
            return OperationCategory.INTEGRATION;
        }
        if (this == CONFIG || this == HEALTH_CHECK) {
            return OperationCategory.SYSTEM;
        }
        return OperationCategory.BUSINESS;
    }

    /**
     * Operation categories for grouping related operations
     */
    public enum OperationCategory {
        READ("Read Operations"),
        WRITE("Write Operations"),
        SECURITY("Security Operations"),
        INTEGRATION("Integration Operations"),
        SYSTEM("System Operations"),
        BUSINESS("Business Operations");

        private final String description;

        OperationCategory(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name(), description);
    }
}

/**
 * Usage Examples:
 *
 * // Manual specification
 * StoreOperation operation = StoreOperation.CREATE;
 *
 * // Auto-detection from HTTP method and URL
 * StoreOperation autoOp = StoreOperation.fromHttpMethodAndUrl("POST", "/api/users");
 * // Returns: StoreOperation.CREATE
 *
 * StoreOperation searchOp = StoreOperation.fromHttpMethodAndUrl("GET", "/api/users/search");
 * // Returns: StoreOperation.SEARCH
 *
 * StoreOperation authOp = StoreOperation.fromHttpMethodAndUrl("POST", "/auth/login");
 * // Returns: StoreOperation.AUTHENTICATE
 *
 * // Checking operation characteristics
 * if (operation.isReadOnly()) {
 *     // Handle read-only operations
 * }
 *
 * if (operation.isSecurityOperation()) {
 *     // Add extra security logging
 * }
 *
 * // Getting operation category
 * StoreOperation.OperationCategory category = operation.getCategory();
 * log.info("Operation category: {}", category.getDescription());
 */
