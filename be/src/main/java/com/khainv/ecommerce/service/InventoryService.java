package com.khainv.ecommerce.service;

import com.khainv.ecommerce.config.KafkaTopicConfig;
import com.khainv.ecommerce.dto.order.OrderEvent;
import com.khainv.ecommerce.enums.OrderStatusEnum;
import com.khainv.ecommerce.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Lắng nghe sự kiện tạo đơn hàng
    @KafkaListener(topics = KafkaTopicConfig.ORDER_TOPIC, groupId = "inventory_group")
    public void handleOrderCreated(OrderEvent orderEvent) {
        System.out.println("INVENTORY-SERVICE: Nhận được yêu cầu kiểm tra kho cho đơn hàng: " + orderEvent.getOrderId());

        // Mô phỏng logic kiểm tra kho
        boolean inStock = Math.random() > 0.1; // 90% là có hàng
        OrderEvent responseEvent = new OrderEvent();
        ObjectMapperUtils.map(orderEvent, responseEvent);

        if (inStock) {
            responseEvent.setStatus(OrderStatusEnum.INVENTORY_RESERVED);
            System.out.println("INVENTORY-SERVICE: Đã giữ hàng cho đơn hàng: " + orderEvent.getOrderId());
        } else {
            responseEvent.setStatus(OrderStatusEnum.INVENTORY_OUT_OF_STOCK);
            System.out.println("INVENTORY-SERVICE: Hết hàng cho đơn hàng: " + orderEvent.getOrderId());
        }

        // Gửi sự kiện phản hồi lại
        kafkaTemplate.send(KafkaTopicConfig.ORDER_EVENTS_TOPIC, responseEvent.getOrderId().toString(), responseEvent);
    }
}
