package com.mekari.mokaaddons.webhookconsumer.publisher;

import java.util.Map;

import com.mekari.mokaaddons.common.publisher.Publisher;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqPublisher implements Publisher {
    private @Autowired AmqpTemplate amqpTemplate;
    
    @Override
    public void publish(String topic, Object message) {
        amqpTemplate.convertAndSend(topic, null, message);
    }

    @Override
    public void publish(String topic, Object message, Map<String, String> properties) {
        var routingKey = properties.get("routingKey");
        amqpTemplate.convertAndSend(topic, routingKey, message);
        
    }
}
