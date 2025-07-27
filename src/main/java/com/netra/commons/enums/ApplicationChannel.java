package com.netra.commons.enums;

import java.util.Arrays;

public enum ApplicationChannel {
    USER_WEB_PORTAL("Free user website "),
    USER_MOBILE_PORTAL("Free user mobile App"),

    API_CLIENT(""),
    API_INTERNAL(""),
    INTERNAL_ADMIN_CONSOLE(""),
    CLIENT_PORTAL_ADMIN(""),

    SYSTEM_JOB("");

    private final String description;
    ApplicationChannel(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

}

