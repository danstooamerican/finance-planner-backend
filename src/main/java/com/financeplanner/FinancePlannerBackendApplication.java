package com.financeplanner;

import com.financeplanner.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class FinancePlannerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancePlannerBackendApplication.class, args);
    }

}