package com.khainv.ecommerce.entity;

import com.khainv.ecommerce.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity  extends AbstractEntity<Long> {
    @Column(name = "user_id", nullable = false)
    private Long userId;

//    @Column(name = "shipping_address", columnDefinition = "jsonb")
//    private String shippingAddress; // Lưu snapshot của địa chỉ

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status")
    private OrderStatusEnum status;

    @Transient
    private List<OrderItem> items;
}
