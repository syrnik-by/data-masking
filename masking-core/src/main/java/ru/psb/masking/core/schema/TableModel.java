package ru.psb.masking.core.schema;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class TableModel {
    String schema;
    String name;
    List<ColumnModel> columns;
}
