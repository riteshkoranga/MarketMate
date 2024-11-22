package com.ecommerce.MarketMate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.MarketMate.model.productOrder;

public interface productOrderRepository extends JpaRepository<productOrder,Integer>{

    List<productOrder> findByUserIdOrderByIdDesc(Integer userId);

    productOrder findByOrderId(String orderId);

}
