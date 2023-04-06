package com.batch_drop.batch.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final String password = "Password";
    static{
        System.setProperty(password,"1234");
    }
    private static final String username = "postgres";
    private static final String db_name = "transaction_db";
    private static String db_link = "127.0.0.1";

//    private final String db_link = "localhost";

    private static final String port = "5432";
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://"+db_link+":" + port +"/"+db_name)
                .username(username)
                .password(System.getProperty(password))
                .build();
    }

}
