package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class FormField {
    private String name;
    private FieldType type = FieldType.STRING;
    private boolean required = false;
    private String label;

    enum FieldType { STRING, NUMBER, BOOLEAN, DATE }
}
