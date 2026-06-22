package ru.yourcompany.masking.config.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.yourcompany.masking.common.exception.MaskingException;
import ru.yourcompany.masking.config.MaskingConfig;

import java.io.File;
import java.io.IOException;

public class YamlConfigLoader {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public MaskingConfig load(String path) {
        try {
            return mapper.readValue(new File(path), MaskingConfig.class);
        } catch (IOException e) {
            throw new MaskingException("Failed to load config from: " + path, e);
        }
    }
}
