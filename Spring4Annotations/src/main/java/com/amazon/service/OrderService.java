package com.amazon.service;

import com.amazon.model.Order;
import com.amazon.repository.OrderRepository;
import com.amazon.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service    //Spring Container identifies it as Servicelayer class / Buisness Logic / Registered in the container Via Componet Scan by Identifiying thrigh @Service annotation and Refistered in Spring Container as Camel Case ("orderService") as Bean ID/Name
public class OrderService {


    @Autowired     // Flow Explained --> 1. In AppConfig.java Annotation ComponentScan(basePackages:{"com.amazon"}) --> Spring Container Start Scanning the Package and its SubPackages for ANnotation
     // That includes Component and its Child Annotation ex:-> @Service , @Repository , etc... this annotation tells that They should be registered As an SPring beans in the Spring Container ...
    // 2.After registration of OrderRepository using Annotation @Repository  is done ;;; In MAin.java   @AutoWired Annotation Injects the OrderRepository Object Automatically in the Dependent/Target class i.e., here Dependent ::"OrderService"  Dependency ::"MessageService" , "OrderRepository"
     // The Injection is by defualt of Type :: "byType" ;{"This concept comes in (autowiring) in spring Core"} Read that From Notes ; Setter Injection is used to inject;
    //byType : Spring Container compares the parameter Type(OrderRepository) and Class(In Spring Container)com.amazon.repository.OrderRepository->This  is compared<- Below Reference Variable orderRepo Type::
    private OrderRepository orderRepo;

    @Autowired
    private MessageService messageService;

    public void createOrder(Order order)
    {

        boolean flag = orderRepo.orderSave(order);

        if(flag)
        { boolean messageSend = messageService.sendMessage(order);

            if(messageSend){
                System.out.println("Successfully completed the process...");
            }
            else{
                System.out.println("Will send the email in 3 Buisness days due to some issue could not send now");
            }
        }


    }





}
