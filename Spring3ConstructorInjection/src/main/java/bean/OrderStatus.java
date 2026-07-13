package bean;

import java.util.Scanner;

public class OrderStatus {

    private DelieveryStatus delieveryStatus;
    private KitchenStatus kitchenStatus;



    public OrderStatus(DelieveryStatus delieveryStatus ,KitchenStatus kitchenStatus ){
        this.delieveryStatus= delieveryStatus;
        this.kitchenStatus = kitchenStatus;

    }


    public void getOrderStatus(){

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your orderID : ");
        String orderId = sc.next();

         System.out.println("OrderStatus :  \n " );
        delieveryStatus.findDelieverystatus(orderId);
        kitchenStatus.findKitchenStatus(orderId);


    }



}
