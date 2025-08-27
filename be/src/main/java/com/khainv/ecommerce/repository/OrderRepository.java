package com.khainv.ecommerce.repository;

import com.khainv.ecommerce.entity.OrderEntity;
import com.khainv.ecommerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
