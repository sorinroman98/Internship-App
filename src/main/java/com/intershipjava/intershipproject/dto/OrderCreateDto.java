package com.intershipjava.intershipproject.dto;

import com.intershipjava.intershipproject.model.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class OrderCreateDto {
    private Customer customer;
    private List<String> idProducts;

    public OrderCreateDto() {

    }

    public OrderCreateDto(Customer customer, List<String> idProducts) {
        this.customer = customer;
        this.idProducts = idProducts;
    }
}
