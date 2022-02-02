package com.intershipjava.intershipproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class AddProductsToOrderRequest {
    private String uuidProduct;
    private String email;
    private String name;

}
