package com.ecommerce.MarketMate.model;


import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class orderRequest {
  

    private String firstName;
    private String lastName;
    private String email;
    
    private String mobileNo;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String paymentType;

}
