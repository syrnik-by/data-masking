package ru.psb.masking.config.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import ru.psb.masking.common.exception.MaskingException;
import ru.psb.masking.config.MaskingConfig;

import java.io.File;
import java.io.IOException;

@Slf4j
public class YamlConfigLoader {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public MaskingConfig load(String path) {
        log.info("Loading config from: {}", path);
        try {
            return mapper.readValue(new File(path), MaskingConfig.class);
        } catch (IOException e) {
            throw new MaskingException("Failed to load config from " + path, e);
        }
    }
}
