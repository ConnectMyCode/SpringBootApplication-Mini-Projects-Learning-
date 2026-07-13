package org.Spring.core.learn.main;

import org.Spring.core.learn.bean.HelloWorld;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class main {
    public static void main(String []args ){

      //Start the IOC Container.
      ApplicationContext ctx = new ClassPathXmlApplicationContext("config.xml");
      //As Discussed there are 2 types of Containers >> 1.ApplicationContext 2.BeanFactory these both are Container Interfaces .. They are implemented by classes whose object is created and inside ".xnl" file is given as a parameter.
      //Interface > ApplicationContext >> Container Logic >> Implemented by >> ClassPathXmlApplicationContext class , 2 more classes
      //Purpose of above liine of Code : To LOad the Container so the Object Beans Are Created automatically once the container is loaded and storred in Bean Container Cache .

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name : ");
        String name = sc.next();

        //get the bean from the container.
        Object obj = ctx.getBean("helloWorld");  //Gets the Objects from inside the container  >> HelloWorld bean Obect
        HelloWorld world =  (HelloWorld)obj; //TypeCasted from Object to HelloWorld

        world.sayHello(name);



    }

}
