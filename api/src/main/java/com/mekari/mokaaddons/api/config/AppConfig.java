package com.mekari.mokaaddons.api.config;

import java.util.TimeZone;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.mokaadons-datasource")
    public DataSource mokaAddOnsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "eventstore")
    @ConfigurationProperties("spring.eventstore-datasource")
    public DataSource eventStoreDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .setTimeZone(TimeZone.getTimeZone("UTC"))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);
        // .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        // .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    }
}
