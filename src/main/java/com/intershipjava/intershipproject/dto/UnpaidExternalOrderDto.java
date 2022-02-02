package com.intershipjava.intershipproject.dto;


public class UnpaidExternalOrderDto extends OrderDto {
    public UnpaidExternalOrderDto(OrderDto dto) {
        this.setOrderId(dto.getOrderId());
        this.setCustomerName(dto.getCustomerName());
        this.setCustomerEmail(dto.getCustomerEmail());
        this.setId(dto.getId());
        this.setLocalDateTime(dto.getLocalDateTime());
        this.setProductDtoList(dto.getProductDtoList());
        this.setTotalAmount(dto.getTotalAmount());
        this.setExternal(true);
        this.setPaid(false);
    }


}