package com.intershipjava.intershipproject.factory;

import com.intershipjava.intershipproject.dto.OrderDto;
import com.intershipjava.intershipproject.dto.PaidInternalOrderDto;
import com.intershipjava.intershipproject.dto.UnpaidInternalOrderDto;

public class InternalOrderFactory extends AbstractOrderFactory {


    @Override
    public OrderDto getOrderDto(OrderDto orderDto, boolean isPaid) {
        if (isPaid) {
            return new PaidInternalOrderDto(orderDto);
        } else {
            return new UnpaidInternalOrderDto(orderDto);
        }
    }
}

