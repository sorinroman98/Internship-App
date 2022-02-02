package com.intershipjava.intershipproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_Product")
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "category")
    private String category;
    @Column(name = "productName")
    private String productName;
    @Column(name = "price")
    private double price;
    @Column(name = "issues")
    private String issues;
    @Column(name = "quantity")
    private int quantity;

    @ManyToMany(mappedBy = "productList")
    private List<Order> orderList = new ArrayList<>();

}
