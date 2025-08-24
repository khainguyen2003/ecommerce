package com.khainv.ecommerce.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @KafkaListener(topics = "order-topic", groupId = "notification-group")
    public void notifyOrder(String message) {
        log.info(String.format("📬 [Notification Group] Received message: %s", message));
    }
}
