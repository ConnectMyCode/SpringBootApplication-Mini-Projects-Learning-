package bean;

import entity.Delievery;

public class DelieveryStatus {

    public  void findDelieverystatus(String orderId){

        if(orderId.equals("123-456")){
            Delievery delievery = new Delievery("12:30 Pm" ,"On the Way");
            System.out.println( delievery );
            }
        else if(orderId.equals("567-8910")){
            Delievery delievery1 = new Delievery("2:49 Pm","Arrived");
            System.out.println(delievery1);
        }
        else{
            System.out.println("Invalid Order Id please check if entered correctly");
        }
    }




}
