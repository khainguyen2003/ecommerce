package com.khainv.ecommerce.dto.order;

import lombok.Data;

@Data
public class OrderItem {
    private Long productId;
    private Integer quantity;
    private Double price;
}
