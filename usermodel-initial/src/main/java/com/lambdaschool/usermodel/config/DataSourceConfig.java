package com.lambdaschool.usermodel.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
@Configuration
public class DataSourceConfig {
    @Value("${local.run.db:H2}")
    private String dbValue;

    @Value("${spring.datasource.url:}")
    private String dbURL;

    @Bean
    public DataSource dataSource() {
        if (dbValue.equalsIgnoreCase("POSTGRESQL")) {
            //Assume Heroku
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(dbURL);
            return new HikariDataSource(config);
        } else {
            //Assume H2
            String URLString = "jbdc:h2:mem:testdb";
            String DriverClass = "org.h2.Driver";
            String DBUser = "sa";
            String DBpass = "";

            return DataSourceBuilder.create()
                    .username(DBUser)
                    .password(DBpass)
                    .url(URLString)
                    .driverClassName(DriverClass)
                    .build();
        }
    }
}
