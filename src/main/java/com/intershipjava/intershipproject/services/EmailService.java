package com.intershipjava.intershipproject.services;

import com.intershipjava.intershipproject.model.Customer;

public interface EmailService {

    void sendVerificationToken(String token, Customer user);
}
