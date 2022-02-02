package com.intershipjava.intershipproject.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDto {

    private Long id;
    private String uuid;
    private String productName;
    private String category;
    private double price;
    private String issues;
    private int quantity;

}
