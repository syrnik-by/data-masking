package ru.psb.masking.engine.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.psb.masking.config.profile.ConnectionProfile;

import javax.sql.DataSource;

/**
 * Creates a pooled {@link DataSource} from a {@link ConnectionProfile}.
 * Each call produces a new HikariCP pool — callers are responsible for closing it
 * (HikariDataSource implements Closeable).
 */
public class DataSourceFactory {

    public DataSource create(ConnectionProfile profile) {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(profile.getUrl());
        cfg.setUsername(profile.getUsername());
        cfg.setPassword(profile.getPassword());
        cfg.setMaximumPoolSize(5);
        cfg.setConnectionTimeout(10_000);
        cfg.setPoolName("masking-pool-" + profile.getDialect());
        return new HikariDataSource(cfg);
    }
}
