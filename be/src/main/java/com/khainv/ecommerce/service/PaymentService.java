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
public class PaymentService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Lắng nghe sự kiện tạo đơn hàng
    @KafkaListener(topics = KafkaTopicConfig.ORDER_TOPIC, groupId = "payment_group")
    public void handleOrderCreated(OrderEvent orderEvent) {
        System.out.println("PAYMENT-SERVICE: Nhận được yêu cầu thanh toán cho đơn hàng: " + orderEvent.getOrderId());

        // Mô phỏng logic xử lý thanh toán
        boolean paymentSuccess = orderEvent.getTotalAmount() < 1000; // Giả sử đơn hàng > 1000 là thất bại

        OrderEvent responseEvent = new OrderEvent();
        ObjectMapperUtils.map(orderEvent, responseEvent);

        if (paymentSuccess) {
            responseEvent.setStatus(OrderStatusEnum.PAYMENT_SUCCESSFUL);
            System.out.println("PAYMENT-SERVICE: Thanh toán thành công cho đơn hàng: " + orderEvent.getOrderId());
        } else {
            responseEvent.setStatus(OrderStatusEnum.PAYMENT_FAILED);
            System.out.println("PAYMENT-SERVICE: Thanh toán thất bại cho đơn hàng: " + orderEvent.getOrderId());
        }

        // Gửi sự kiện phản hồi lại
        kafkaTemplate.send(KafkaTopicConfig.ORDER_EVENTS_TOPIC, responseEvent.getOrderId().toString(), responseEvent);
    }
}
