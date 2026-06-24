package ru.psb.masking.engine.discovery;

import ru.psb.masking.core.schema.SchemaModel;

public interface SchemaDiscoveryService {
    SchemaModel discover(String configPath);
}
