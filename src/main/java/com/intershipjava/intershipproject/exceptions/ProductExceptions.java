package com.intershipjava.intershipproject.exceptions;

public class ProductExceptions extends RuntimeException {

    String ERRCod;
    public ProductExceptions(String message) {
        super(message);
    }

    public ProductExceptions() {

    }
}
