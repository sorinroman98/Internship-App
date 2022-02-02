package com.intershipjava.intershipproject.services.implement;

import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.model.AbstractToken;
import com.intershipjava.intershipproject.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VerificationTokenImpl {
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public AbstractToken findByToken(String token){
        return verificationTokenRepository.findByToken(token);
    }

    @Transactional
    public AbstractToken findByUser(Customer customer){
        return verificationTokenRepository.findByCustomer(customer);
    }

    @Transactional
    public void save(Customer customer,String token){
//        AbstractToken verificationToken = new AbstractToken(token,customer);
//        verificationToken.setExpiryDate(calculateExpiryDate(30));
//        verificationTokenRepository.save(verificationToken);
    }

}
