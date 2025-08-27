package com.khainv.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    public static final String ORDER_TOPIC = "order";
    public static final String ORDER_EVENTS_TOPIC = "order";
    public static final String CONFIRM_ACCOUNT_TOPIC = "confirm_account_topic";

    @Bean
    public NewTopic confirmAccount() {
        return new NewTopic(CONFIRM_ACCOUNT_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic order() {
        return TopicBuilder.name(ORDER_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderEvents() {
        return TopicBuilder.name(ORDER_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
