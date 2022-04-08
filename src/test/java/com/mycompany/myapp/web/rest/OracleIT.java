package com.mycompany.myapp.web.rest;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.OracleContainer;

public class OracleIT {

    @Rule
    public static OracleContainer oracleContainer = new OracleContainer("oracleinanutshell/oracle-xe-11g:1.0.0");

    @BeforeAll
    public static void startup() {
        oracleContainer.start();
    }

    @TestConfiguration
    static class OracleTestConfiguration {

        @Bean
        DataSource dataSource() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(oracleContainer.getJdbcUrl());
            hikariConfig.setUsername(oracleContainer.getUsername());
            hikariConfig.setPassword(oracleContainer.getPassword());

            return new HikariDataSource(hikariConfig);
        }
    }
}
