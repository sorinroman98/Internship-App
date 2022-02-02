package com.intershipjava.intershipproject.repository;

import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByCustomer(Customer customer);
}
