package com.intershipjava.intershipproject.dto;


public class UnpaidInternalOrderDto extends OrderDto {
    public UnpaidInternalOrderDto(OrderDto dto) {

        this.setOrderId(dto.getOrderId());
        this.setCustomerName(dto.getCustomerName());
        this.setCustomerEmail(dto.getCustomerEmail());
        this.setId(dto.getId());
        this.setLocalDateTime(dto.getLocalDateTime());
        this.setProductDtoList(dto.getProductDtoList());
        this.setTotalAmount(dto.getTotalAmount());
        this.setExternal(false);
        this.setPaid(false);
    }


}
