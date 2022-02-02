package com.intershipjava.intershipproject.services;

import com.intershipjava.intershipproject.dto.CustomerDto;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;
import com.intershipjava.intershipproject.model.Customer;

import java.util.List;

public interface CustomerService {
    boolean setValidEmail(String email);

    boolean setValidName(String name);

    CustomerDto saveCustomer(Customer customer);

    List<Customer> listAllCustomers() throws CustomerExceptions;

    Customer customerDtoToCustomer(CustomerDto customerDto);

    String encryptPassword(String password);

    void createVerificationTokenForUser(Customer user, String token);

    boolean resendVerificationToken(String token);

    String validateVerificationToken(String token);

    CustomerDto customerToDto(Customer customer);

    CustomerDto verifyCustomer(CustomerDto customerDto);
}