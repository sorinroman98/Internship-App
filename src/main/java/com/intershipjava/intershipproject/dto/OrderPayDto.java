package com.intershipjava.intershipproject.dto;

import com.intershipjava.intershipproject.model.CreditCard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderPayDto {
    private CreditCard creditCard;
    private String idOrder;

}
