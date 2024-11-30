package com.ecommerce.MarketMate.util;

public enum BucketType {
    CATEGORY(1,""),
    PRODUCT(2,""),
    PROFILE(3,"");
    

    BucketType(int id, String name) {
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
