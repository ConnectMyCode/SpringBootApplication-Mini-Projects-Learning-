package bean;

import entity.Kitchen;

public class KitchenStatus {

    public void findKitchenStatus(String orderId){

        if(orderId.equals("123-456")){

            Kitchen kitchen  = new Kitchen("preparing" , "FriedRice", orderId);
             System.out.println(kitchen.toString());
        }
        else if(orderId.equals("567-8910")){
           Kitchen kitchen =  new Kitchen("Prepared", "Non Veg Biryani" , orderId);
             System.out.println(kitchen.toString());
        }
        else{
            System.out.println("Invalid Order id please verify and enter again... ");
        }
    }

}
