package com.khainv.ecommerce.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
    private List<OrderItem> items;
    private BigDecimal totalPrice;
    private BigDecimal totalAmount;
}
