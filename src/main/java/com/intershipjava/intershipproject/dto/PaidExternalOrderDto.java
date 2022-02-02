package com.intershipjava.intershipproject.dto;


public class PaidExternalOrderDto extends OrderDto {
    public PaidExternalOrderDto(OrderDto dto) {

        this.setId(dto.getId());
        this.setOrderId(dto.getOrderId());
        this.setCustomerEmail(dto.getCustomerEmail());
        this.setCustomerName(dto.getCustomerName());
        this.setLocalDateTime(dto.getLocalDateTime());
        this.setProductDtoList(dto.getProductDtoList());
        this.setTotalAmount(dto.getTotalAmount());
        this.setExternal(true);
        this.setPaid(true);
    }

}
