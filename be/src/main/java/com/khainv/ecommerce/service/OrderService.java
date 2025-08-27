package com.khainv.ecommerce.service;

import com.khainv.ecommerce.config.KafkaTopicConfig;
import com.khainv.ecommerce.dto.order.OrderEvent;
import com.khainv.ecommerce.dto.order.OrderRequest;
import com.khainv.ecommerce.entity.OrderEntity;
import com.khainv.ecommerce.enums.OrderStatusEnum;
import com.khainv.ecommerce.repository.OrderRepository;
import com.khainv.ecommerce.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderRepository orderRepository;

    public void createOrder(OrderRequest orderRequest) {
        OrderEntity orderEntity = ObjectMapperUtils.map(orderRequest, OrderEntity.class);
        orderEntity.setStatus(OrderStatusEnum.AWAITING_VERIFICATION);
        orderEntity = orderRepository.save(orderEntity);
        OrderEvent orderEvent = ObjectMapperUtils.map(orderEntity, OrderEvent.class);
        orderEvent.setOrderId(orderEntity.getId());
        orderEvent.setTotalPrice(orderRequest.getTotalPrice());


        this.kafkaTemplate.send(KafkaTopicConfig.ORDER_TOPIC, orderEvent);
    }

    @KafkaListener(topics = KafkaTopicConfig.ORDER_EVENTS_TOPIC, groupId = "orders_group")
    public void handleOrderEvents(OrderEvent event) {
        System.out.println("ORDER-SERVICE: Nhận được sự kiện: " + event.getStatus().getValue() + " cho đơn hàng " + event.getOrderId());

        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            if(OrderStatusEnum.PAYMENT_SUCCESSFUL.equals(event.getStatus()) || OrderStatusEnum.INVENTORY_RESERVED.equals(event.getStatus())) {
                order.setStatus(OrderStatusEnum.PROCESSING);
                orderRepository.save(order);
                System.out.println("ORDER-SERVICE: Cập nhật trạng thái đơn hàng " + order.getId() + " thành CONFIRMED");
            } else if(OrderStatusEnum.PAYMENT_FAILED.equals(event.getStatus()) || OrderStatusEnum.INVENTORY_OUT_OF_STOCK.equals(event.getStatus())) {
                order.setStatus(OrderStatusEnum.CANCELLED);
                orderRepository.save(order);
                System.out.println("ORDER-SERVICE: Cập nhật trạng thái đơn hàng " + order.getId() + " thành CANCELLED");
            }
        });
    }
}
