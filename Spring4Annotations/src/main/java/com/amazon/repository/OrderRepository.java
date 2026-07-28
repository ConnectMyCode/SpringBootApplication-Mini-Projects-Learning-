package com.amazon.repository;

import com.amazon.model.Order;
import org.springframework.stereotype.Repository;


@Repository    //Spring container while scanning through packages will identify this tag and register this class as Bean in spring Container ; It represents this is Database Logic Layer ... Persistent Layer
public class OrderRepository {

    private Order order;

    public boolean orderSave(Order order)
    {
        //DB connection and Logic to save the data Into database via queries...

        System.out.println("Order is saved");

        System.out.println("Order id :" + order.getOrderId() + "Order name : " +order.getOrderName() + " Order Price : " + order.getOrderPrice());

        return true;
        
    }



}
