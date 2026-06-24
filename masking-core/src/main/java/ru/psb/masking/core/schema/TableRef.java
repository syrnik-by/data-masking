package ru.psb.masking.core.schema;

import lombok.Value;

@Value
public class TableRef {
    String schema;
    String name;

    @Override
    public String toString() {
        return schema + "." + name;
    }
}
