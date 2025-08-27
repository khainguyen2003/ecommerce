package com.khainv.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends AbstractEntity<Long> {
    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "images")
    private String images;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "attr", columnDefinition = "JSONB")
    private String attr;

    @Column(name = "avg_rate", precision = 2, scale = 1)
    private BigDecimal avgRate;

    @Column(name = "review_cnt")
    private Integer reviewCnt;

    @Column(name = "status")
    private Byte status;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "owner_name")
    private String ownerName;
}
