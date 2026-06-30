package ru.psb.masking.config.profile;

import lombok.Data;

@Data
public class ConnectionProfile {
    private String url;
    private String username;
    private String password;
    private String dialect;
    /** Target schema name to read/write. Defaults to 'public' if not specified. */
    private String schema = "public";
}
