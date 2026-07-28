package org.Spring.core.learn.main;

import org.Spring.core.learn.bean.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner



public class main {
    public static void main(String []args){
        ///1.Place an Order --> Create am OrderService Object  == Instantiate OrderService
        /// Call the method --> PlaceOrder() method of OrderService() Instance. --> Pass String ProductName , Integer ProductQuantity
        /// Print the message

        Scanner sc = new Scanner(System.in);


        System.out.println("Enter Product name: ");
        String productName =  sc.next();

        System.out.println("Enter Product quantity want to purchase : ");
        Integer quantity = sc.nextInt();


      //Container Loaded , Creates Bean Objects automatically , Injection , Keeps Object Alive , Deletes the Object .
      ApplicationContext xav = new ClassPathXmlApplicationContext("config.xml");

     Object obj =  xav.getBean("os");
     OrderService orderService = (OrderService)obj;

     //Initializing the fields by Setting manually using setter methods not with the help of Spring container
     orderService.setProductName(productName);

     orderService.setProductQuantity(quantity);
     //Calling Method to place A order
     orderService.placeOrder();


    /* Q. When exactly the setter method is called is it called when we want to retrieve the object from the container or it happens auto in container itself */
     //  👉 Setter injection happens during bean creation phase, not retrieval phase

    //Flow of the Spring Container  (Bean Creation Phase)
        /*
       First : Instantiated OrderService
         2nd : Instantiated InventoryService
         3rd : Setter method is used to Inject Dependency Object inside dependent Class using Setter method of Dependent Class...



        * */




/**
        //NOTES ON Config.xml file ;  writing a container File

        🔥 How <property name=""> maps to setter


        Spring follows JavaBean naming convention.



                Rule:

        property name = inventoryService



        Spring converts it to:

        set + Capitalize(first letter)



→ setInventoryService()



        Bean tag:

        "id" → any unique String, used to retrieve bean via getBean(id)

        "class" → fully qualified class name



                <property> tag:

        Purpose → used for setter injection



        Attributes:

        - name → must match property/setter name (inventoryService → setInventoryService())

                - ref → must match another bean id (dependency object)

                - value → used for primitive/String values





🔥 Constructor Injection (<constructor-arg>)



        Purpose → used to inject dependency via constructor



        Example:

<constructor-arg ref="inventoryService"/>



                Attributes:

        - ref → used when injecting object (bean id)

        - value → used for primitive/String values

                - index → specifies position of parameter (0,1,2...)

        - type → specifies data type (used when multiple constructors exist)

                - name → matches constructor parameter name (optional, requires -parameters flag)



        Mapping:

        Spring directly calls constructor:

        OrderService(InventoryService inventoryService)



        NOTE:

        - In constructor injection, no setter is required

                - Injection happens at object creation time

*/


    }
}