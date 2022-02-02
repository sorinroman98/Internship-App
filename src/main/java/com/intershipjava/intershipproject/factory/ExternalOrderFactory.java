package com.intershipjava.intershipproject.factory;

import com.intershipjava.intershipproject.dto.OrderDto;
import com.intershipjava.intershipproject.dto.PaidExternalOrderDto;
import com.intershipjava.intershipproject.dto.UnpaidExternalOrderDto;


public class ExternalOrderFactory extends AbstractOrderFactory {


    @Override
    public OrderDto getOrderDto(OrderDto orderDto, boolean isPaid) {
        if (isPaid) {
            return new PaidExternalOrderDto(orderDto);
        } else {
            return new UnpaidExternalOrderDto(orderDto);
        }
    }
}

