package com.ecommerce.MarketMate.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class productOrder {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String orderId;
    private Date orderDate;

    @ManyToOne
    private Product product;
    private Double price;
    private Integer quantity;

    @ManyToOne
    private userDetails user;
    private String status;
    private String paymentType;

    @OneToOne(cascade = CascadeType.ALL)
    private orderAddress orderAddress;
    

}
