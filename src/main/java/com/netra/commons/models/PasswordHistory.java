package com.netra.commons.models;

import lombok.Data;

@Data
public class PasswordHistory extends BaseEntity{
    private Identity identity;
    private String hashedPassword;
}
