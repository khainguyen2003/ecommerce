package com.khainv.ecommerce.service;

import com.khainv.ecommerce.config.KafkaTopicConfig;
import com.khainv.ecommerce.dto.order.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @KafkaListener(topics = KafkaTopicConfig.ORDER_EVENTS_TOPIC, groupId = "notification_group")
    public void handleNotificationEvents(OrderEvent event) {
        // Dịch vụ này lắng nghe các sự kiện và gửi thông báo tương ứng

        String message = "";
        switch (event.getStatus()) {
            case PAYMENT_SUCCESSFUL:
                message = String.format(
                        "Gửi email xác nhận thanh toán thành công cho đơn hàng %s tới khách hàng %s.",
                        event.getOrderId(), event.getUserId()
                );
                break;
            case INVENTORY_RESERVED:
                message = String.format(
                        "Gửi thông báo nội bộ: Kho đã giữ hàng thành công cho đơn hàng %s.",
                        event.getOrderId()
                );
                break;
            case PAYMENT_FAILED:
                message = String.format(
                        "Gửi email thông báo thanh toán thất bại cho đơn hàng %s tới khách hàng %s.",
                        event.getOrderId(), event.getUserId()
                );
                break;
            case INVENTORY_OUT_OF_STOCK:
                message = String.format(
                        "Gửi email xin lỗi khách hàng %s vì sản phẩm trong đơn hàng %s đã hết hàng.",
                        event.getUserId(), event.getOrderId()
                );
                break;
            default:
                // Không làm gì với các sự kiện khác
                return;
        }

        System.out.println("NOTIFICATION-SERVICE: " + message);
    }
}
