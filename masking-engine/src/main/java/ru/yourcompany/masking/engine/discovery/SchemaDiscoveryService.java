package ru.yourcompany.masking.engine.discovery;

import ru.yourcompany.masking.core.schema.SchemaModel;

public interface SchemaDiscoveryService {
    SchemaModel discover(String configPath);
}
