package com.financeplanner;

import com.financeplanner.config.AppProperties;
import com.financeplanner.datasource.DatabaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.sql.DataSource;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class FinancePlannerBackendApplication {

    public FinancePlannerBackendApplication(DataSource dataSource) {
        DatabaseInitializer.initializeDatabase(dataSource);
    }

    public static void main(String[] args) {
        SpringApplication.run(FinancePlannerBackendApplication.class, args);
    }

}
