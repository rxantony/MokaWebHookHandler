package com.mekari.mokaaddons.common.messaging;

import java.util.Map;

public interface Publisher {
    void publish(String topic, Object message);

    void publish(String topic, Object message, Map<String, String> properties);
}