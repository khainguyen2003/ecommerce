package com.khainv.ecommerce.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.khainv.ecommerce.converter.AbstractEnumConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum implements BaseEnum {
    PENDING_PAYMENT(1, "pending_payment"), // Chờ thanh toán, áp dụng cho các phương thức như chuyển khoản ngân hàng, ví điện tử, hoặc thanh toán qua cổng online chưa thành công
    AWAITING_VERIFICATION(2, "awaiting_verification"), // chờ hệ thống hoặc người bán đang chờ để xác nhận tính hợp lệ của đơn hàng
    PROCESSING(3, "processing"), // Đơn hàng đã hợp lệ và người bán đang chuẩn bị hàng
    AWAITING_PICKUP(4, "awaiting_pickup"), // Người bán đã đóng gói xong và sẵn sàng bàn giao cho đơn vị vận chuyển đến lấy
    IN_TRANSIT(5, "in_transit"), // Đơn hàng đang trên đường vận chuyển từ kho của người bán đến địa chỉ của khách hàng
    OUT_FOR_DELIVERY(6, "out_for_delivery"), // Bưu tá đang trên đường giao kiện hàng đến cho khách hàng trong ngày hôm đó
    DELIVERED(7, "delivered"),
    DELIVERY_FAILED(8, "delivery_failed"),
    COMPLETED(9, "completed"),
    CANCELLED(10, "cancelled"),
    RETURN_INITIATED(11, "return_initiated"), // Yêu cầu trả hàng
    RETURNED(12, "returned"),
    REFUNDED(13, "refunded"),

    PAYMENT_SUCCESSFUL(14, "payment_successful"),
    PAYMENT_FAILED(15, "payment_failed"),
    INVENTORY_RESERVED(16, "inventory_reserved"),
    INVENTORY_OUT_OF_STOCK(17, "inventory_out_of_stock");

    private Integer key;

    @JsonValue
    private String value;

    @Converter(autoApply = true)
    private static class OrderStatusConverter extends AbstractEnumConverter<OrderStatusEnum> {
        public OrderStatusConverter() {
            super(OrderStatusEnum.class);
        }
    }

    public OrderStatusEnum fromKey(Integer key) {
        if(key == null) {
            return null;
        }
        for (OrderStatusEnum param : OrderStatusEnum.values()) {
            if (param.getKey().equals(key)) {
                return param;
            }
        }
        throw new IllegalArgumentException("Không có enum tương ứng với key: " + key);
    }
}
