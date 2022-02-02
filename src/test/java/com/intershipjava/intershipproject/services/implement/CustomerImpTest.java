package com.intershipjava.intershipproject.services.implement;

import com.intershipjava.intershipproject.dto.CustomerDto;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;
import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.repository.CustomerRepository;
import com.intershipjava.intershipproject.services.validator.ValidatorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerImpTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ValidatorService validator;

    @InjectMocks
    private CustomerImp customerImp;

    @Test
    void setValidEmail_isTrue() {
        when(validator.isValidEmailAddress(any())).thenReturn(true);
        assertTrue(customerImp.setValidEmail("Sorin@yahoo.com"));
    }

    @Test
    void setValidName_isTrue() {
        when(validator.isValidName(any())).thenReturn(true);
        assertTrue(customerImp.setValidName("Sorin"));
    }

    @Test
    void saveCustomer_isTrue() {
        Customer customer = new Customer();
        customer.setEmail("sorinroma@yahoo.com");
        customer.setName("Sorin");
        when(customerRepository.save(customer)).thenReturn(customer);
        CustomerDto customerDto = customerImp.saveCustomer(customer);
        assertTrue(customerDto.getEmail().equals(customer.getEmail()) && customerDto.getName().equals(customer.getName()));
    }

    @Test
    void saveCustomer_throwException() {
        assertThrows(CustomerExceptions.class, () -> {
            customerImp.saveCustomer(null);
        });
    }

    @Test
    void listAllCustomers_throwException() {
        List<Customer> customerList = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customerList);
        assertThrows(CustomerExceptions.class, () -> {
            customerImp.listAllCustomers();
        });
    }

    @Test
    void listAllCustomers_True() {
        Customer customer = new Customer();
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        when(customerRepository.findAll()).thenReturn(customerList);
        assertEquals(customerImp.listAllCustomers(), customerList);
    }

    @Test
    void customerDtoToCustomer_isTrue() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Sorin");
        customerDto.setEmail("sorin@yahoo.com");

        Customer customer = customerImp.customerDtoToCustomer(customerDto);

        assertTrue(customerDto.getEmail().equals(customer.getEmail()) && customerDto.getName().equals(customer.getName()));

    }

    @Test
    void customerDtoToCustomer_throwException() {
        assertThrows(CustomerExceptions.class, () -> {
            customerImp.customerDtoToCustomer(null);
        });
    }

    @Test
    void customerToDto_isTrue() {
        Customer customer = new Customer();
        customer.setName("Sorin");
        customer.setEmail("sorin@yahoo.com");

        CustomerDto customerDto = customerImp.customerToDto(customer);


        assertTrue(customerDto.getEmail().equals(customer.getEmail()) && customerDto.getName().equals(customer.getName()));

    }

    @Test
    void customerToDto_throwException() {
        assertThrows(CustomerExceptions.class, () -> {
            customerImp.customerToDto(null);
        });
    }
}