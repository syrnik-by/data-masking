package ru.yourcompany.masking.config.profile;

import lombok.Data;

@Data
public class ConnectionProfile {
    private String url;
    private String username;
    private String password;
    private String dialect;
    private String schema;
}
