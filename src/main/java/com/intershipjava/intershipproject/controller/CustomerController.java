package com.intershipjava.intershipproject.controller;

import com.intershipjava.intershipproject.constants.Constant;
import com.intershipjava.intershipproject.dto.ApiResponse;
import com.intershipjava.intershipproject.dto.CustomerDto;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;
import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getCustomers")
    public List<Customer> list() {
        return customerService.listAllCustomers();
    }

    @PostMapping("/saveCustomer")
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto customerResponse = customerService.saveCustomer(customerService.customerDtoToCustomer(customerDto));

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    @PostMapping("/verifyCustomer")
    public ResponseEntity<CustomerDto> verifyCustomer(@RequestBody CustomerDto customerDto) throws CustomerExceptions {
        CustomerDto customer = customerService.verifyCustomer(customerDto);
        if (customer != null){
            return new ResponseEntity<>(customer,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/token/verify")
    public ResponseEntity<?> confirmRegistration(@RequestBody String token) {
        final String result = customerService.validateVerificationToken(token);
        return ResponseEntity.ok().body(new ApiResponse(true, result));
    }

    @PostMapping("/token/resend")
    @ResponseBody
    public ResponseEntity<?> resendRegistrationToken(@RequestBody String expiredToken) {
        if (!customerService.resendVerificationToken(expiredToken)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not found!"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(new ApiResponse(true, Constant.AppConstants.SUCCESS));    }
}
