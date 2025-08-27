package com.khainv.ecommerce.controller;

import com.khainv.ecommerce.dto.BaseReponse;
import com.khainv.ecommerce.dto.order.OrderRequest;
import com.khainv.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(name = "order_controller")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseReponse<String>> createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);

        BaseReponse<String> baseReponse = new BaseReponse<>();
        baseReponse.setMessage("Order created");
        baseReponse.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(baseReponse, HttpStatus.CREATED);
    }
}
