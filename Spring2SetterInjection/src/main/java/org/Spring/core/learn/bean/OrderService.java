package org.Spring.core.learn.bean;

public class OrderService {

    InventoryService inventoryService ;
    String productName;
    Integer productQuantity;

//Manually Initializing
//Setter Methods to initialize the product Name and quantity...

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    //Automatic Container will do the Initialization
    //Setter Injection.
    //Setter Method >> Container will use this to Inject the Dependency Object.
    public void setnventoryService(InventoryService ntoryService){
        this.inventoryService = ntoryService;
    }

    public void placeOrder(){

        if(inventoryService.isInStock(productName , productQuantity)) {
            System.out.println("Order is placed successfully...");
        }
        else{
            System.out.println("Order not placed successfully...");
        }

    }

}


