package com.intershipjava.intershipproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity

@Table(name = "orderTable")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "orderUuid")
    private String orderId;
    @Column(name = "customerName")
    private String customerName;
    @Column(name = "customerEmail")
    private String customerEmail;
    @Column(name = "isPayed")
    private boolean isPayed;
    @Column(name = "localDateTime")
    private LocalDateTime localDateTime;
    @Column(name = "external")
    private boolean isExternal;
    @Column(name = "totalAmount")
    private double totalAmount;

    @ManyToMany()
    @JoinTable(
            name = "order_list",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "orderUuid"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "uuid")
    )
    private List<Product> productList = new ArrayList<>();

    public void addProduct(Product product) {
        productList.add(product);
    }
}
