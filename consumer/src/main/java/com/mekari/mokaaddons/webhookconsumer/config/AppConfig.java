package com.mekari.mokaaddons.webhookconsumer.config;

import java.util.TimeZone;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mekari.mokaaddons.common.messaging.rabbitmq.RabbitMQConsumerErrorHandler;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
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
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        var factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setErrorHandler(new RabbitMQConsumerErrorHandler());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ObjectMapper mapper, ConnectionFactory connFactory) {
        var msgConverter = new Jackson2JsonMessageConverter(mapper);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connFactory);
        rabbitTemplate.setMessageConverter(msgConverter);
        return rabbitTemplate;
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