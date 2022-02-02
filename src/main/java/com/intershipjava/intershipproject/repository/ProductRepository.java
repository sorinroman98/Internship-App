package com.intershipjava.intershipproject.repository;

import com.intershipjava.intershipproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("FROM Product WHERE uuid = ?1")
    Product findByUuid(String uuid);

    @Modifying
    @Query("update Product u set u.quantity = u.quantity - 1 where u.uuid = :uuid")
    void updateProductQuantity(@Param(value = "uuid") String uuid);

//    @Modifying
//    @Query("update Product u set u.quantity = :quantity, u.productName = :name, u.price = : price, u.issues =: issues" +
//            ",u.category = :category where u.uuid = :uuid")
//    void updateProductByUuid(@Param(value = "uuid") String uuid,String category, String name,
//                               double price, String issues, int quantity);


    @Query("FROM Product WHERE id = ?1")
    Product findById(int id);
}
