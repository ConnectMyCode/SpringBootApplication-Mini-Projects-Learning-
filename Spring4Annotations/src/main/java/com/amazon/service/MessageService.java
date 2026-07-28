package com.amazon.service;

import com.amazon.model.Order;
import org.springframework.stereotype.Service;

@Service
public class MessageService {


    public boolean sendMessage(Order order)
    {
        System.out.println("Send the order details on email Id: " + order.getEmail());

        return true;

    }

}