package com.ecommerce.MarketMate.util;

public enum orderStatus {
    IN_PROGRESS(1,"In Progress"),
    Order_RECIEVED(2,"Order Recieved"),
    Order_Processed(3,"Product Packed and Shipped"),
    OUT_FOR_DELIVERY(4,"Out For Delivery"),
    DELIVERED(5,"Delivered");

    orderStatus(int id, String name) {
        this.id=id;
        this.name=name;
           
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer id;

    private String name;
    

}
