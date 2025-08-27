package com.khainv.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_cart")
@Getter
@Setter
@AllArgsConstructor
public class CartEntity extends AbstractEntity<Long> {

}
