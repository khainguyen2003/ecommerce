package com.khainv.ecommerce.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalyticsSerivce {

    @KafkaListener(topics = "order-topic", groupId = "analytics-group")
    public void analyticsOrder(String message) {
        log.info(String.format("📊 [Analytics Group] Received message: %s", message));
    }
}
