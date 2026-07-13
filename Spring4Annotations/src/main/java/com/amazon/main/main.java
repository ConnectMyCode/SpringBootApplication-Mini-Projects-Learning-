package com.amazon.main;

import com.amazon.config.AppConfig;
import com.amazon.model.Order;
import com.amazon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class main {
    public static void main(String []args)
    {
        ApplicationContext  context = new AnnotationConfigApplicationContext(AppConfig.class);
        //Loading Spring Container >> This is only needed in the Spring core . In Spring Boot it is automatically Loaded.
        //After loading it will start scanning for Spring Beans and Registering them in container.
        Object obj = context.getBean("orderService");
        OrderService orderService = (OrderService)obj;

        Order order = new Order(111 , "RassBerryPi", 1200D , "arun@gmail.com");



        orderService.createOrder(order);




    }


}
