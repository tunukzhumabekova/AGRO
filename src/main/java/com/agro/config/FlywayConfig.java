package com.agro.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
@Configuration
public class FlywayConfig {
    private final DataSource dataSource;

    public FlywayConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            Flyway.configure()
                    .dataSource(dataSource)
                    .baselineOnMigrate(true) // Enable baselining on migrate
                    .baselineVersion("0") // Set your baseline version
                    .locations("classpath:db.migration") // Adjust your migrations path as needed
                    .defaultSchema("public") // Set your default schema
                    .load()
                    .migrate(); // Trigger migration
        };
    }
}