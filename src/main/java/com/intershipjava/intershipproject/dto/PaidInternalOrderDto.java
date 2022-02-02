package com.intershipjava.intershipproject.dto;


public class PaidInternalOrderDto extends OrderDto {
    public PaidInternalOrderDto(OrderDto dto) {

        this.setOrderId(dto.getOrderId());
        this.setCustomerEmail(dto.getCustomerEmail());
        this.setCustomerName(dto.getCustomerName());
        this.setId(dto.getId());
        this.setLocalDateTime(dto.getLocalDateTime());
        this.setProductDtoList(dto.getProductDtoList());
        this.setTotalAmount(dto.getTotalAmount());
        this.setExternal(false);
        this.setPaid(true);
    }

}
