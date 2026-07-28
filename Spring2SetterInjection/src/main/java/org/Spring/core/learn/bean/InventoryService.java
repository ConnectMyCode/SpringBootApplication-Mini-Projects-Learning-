package org.Spring.core.learn.bean;

import java.util.Map;

public class InventoryService {

    Map< String , Integer> product = Map.of("charger", 12, "Refrigerator",2  ,"phone", 34  );
                                    //map.of({k,v},{k,v},{k,v},{k,v}) => Here without using long syntax of creation a Map Object then inserting the key-value pair by put() method is not needed
                                    //directly all this syntax is skipped and a map is created .
   public boolean isInStock(String name , Integer productQuantity){
    if(product.containsKey(name) && product.get(name) >=  productQuantity ){  //get() method returns value from key-value pair
        System.out.println("Product " + name + "is available : ");
        return true;
    }
    else if(!product.containsKey(name)){
    System.out.println("Product is not available in Store...");
    return false;
    }

    System.out.println("Product is out of stock...");
    return false;
   }


}

