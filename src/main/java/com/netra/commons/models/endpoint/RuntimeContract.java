package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class RuntimeContract {
    private List<FormField> fields = new ArrayList<>(); // additional runtime fields (non-header inputs)
}
