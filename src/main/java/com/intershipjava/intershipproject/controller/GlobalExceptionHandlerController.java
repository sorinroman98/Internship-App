package com.intershipjava.intershipproject.controller;

import com.intershipjava.intershipproject.exceptions.CreditCardExceptions;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;
import com.intershipjava.intershipproject.exceptions.OrderExceptions;
import com.intershipjava.intershipproject.exceptions.ProductExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(value = ProductExceptions.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductExceptions exceptions) {

        return new ResponseEntity<>(exceptions.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = CustomerExceptions.class)
    public ResponseEntity<String> handleCustomerExceptions(CustomerExceptions customerExceptions) {
        return new ResponseEntity<>(customerExceptions.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = CreditCardExceptions.class)
    public ResponseEntity<String> handleCreditCardExceptions(CreditCardExceptions creditCardExceptions) {
        return new ResponseEntity<>(creditCardExceptions.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = OrderExceptions.class)
    public ResponseEntity<String> handleOrderExceptions(OrderExceptions orderExceptions) {
        return new ResponseEntity<>(orderExceptions.getMessage(), HttpStatus.CONFLICT);
    }
}
