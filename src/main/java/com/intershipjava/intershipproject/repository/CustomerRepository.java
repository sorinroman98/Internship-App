package com.intershipjava.intershipproject.repository;

import com.intershipjava.intershipproject.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("FROM Customer WHERE email = ?1 and password = ?2")
    Customer getCustomerByEmailAndPassword(String email, String password);

   // @Query("FROM Customer WHERE email = :email")
    Customer findCustomerByEmail(@Param("email") String email);


}
