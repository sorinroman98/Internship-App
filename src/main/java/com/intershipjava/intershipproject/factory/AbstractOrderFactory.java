package com.intershipjava.intershipproject.factory;

import com.intershipjava.intershipproject.dto.OrderDto;

public abstract class AbstractOrderFactory {
    public static AbstractOrderFactory getFactory(Boolean isInternal) {

        if (isInternal == null) {
            return null;
        }

        if (isInternal) {
            return new InternalOrderFactory();
        } else {
            return new ExternalOrderFactory();
        }
    }

    public abstract OrderDto getOrderDto(OrderDto orderDto, boolean isPaid);
}
