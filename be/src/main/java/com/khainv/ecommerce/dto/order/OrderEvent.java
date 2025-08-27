package com.khainv.ecommerce.dto.order;


import com.khainv.ecommerce.enums.OrderStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private List<OrderItem> items;
    private BigDecimal totalPrice;
    private Long totalAmount;
    private OrderStatusEnum status;
}
