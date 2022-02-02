package com.intershipjava.intershipproject.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(toBuilder = true)
public class OrderDto {
    private long id;
    private String orderId;
    private String customerName;
    private String customerEmail;
    private boolean isPaid;
    private boolean isExternal;
    private LocalDateTime localDateTime;
    private double totalAmount;
    private List<ProductDto> productDtoList = new ArrayList<>();


    public static class Builder {

        private final String orderId;
        private String customerName;
        private String customerEmail;
        private boolean isPaid;
        private LocalDateTime localDateTime;
        private double totalAmount;
        private List<ProductDto> productDtoList = new ArrayList<>();

        public Builder(String orderId) {
            this.orderId = orderId;
        }

        public Builder withCustomerName(String customerName) {
            this.customerName = customerName;

            return this;
        }
        public Builder withCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;

            return this;
        }

        public Builder withIsPaid(boolean isPayed) {
            this.isPaid = isPayed;

            return this;
        }

        public Builder withDate(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;

            return this;
        }

        public Builder withTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;

            return this;
        }

        public Builder withListOfProduct(List<ProductDto> productDtoList) {
            this.productDtoList = productDtoList;

            return this;
        }

        public OrderDto build() {
            OrderDto orderDto = new OrderDto();
            orderDto.customerName = this.customerName;
            orderDto.customerEmail = this.customerEmail;
            orderDto.orderId = this.orderId;
            orderDto.isPaid = this.isPaid;
            orderDto.localDateTime = this.localDateTime;
            orderDto.totalAmount = this.totalAmount;
            orderDto.productDtoList = this.productDtoList;

            return orderDto;
        }
    }


}