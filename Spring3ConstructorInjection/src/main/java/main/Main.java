package main;

import bean.OrderStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class Main {

    public static void main(String []args){

        // Starting thr Spring Container ...

        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationcontext.xml");
        Object obj = ctx.getBean("orderStatus");
        OrderStatus orderStatuss = (OrderStatus)obj;

        orderStatuss.getOrderStatus();
    }
}



