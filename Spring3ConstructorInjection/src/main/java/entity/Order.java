package entity;

public class Order {

    private String orderId;
    private String foodName;

    public Order(String OrderId, String foodName)
    {
        this.orderId = OrderId;
        this.foodName = foodName;
    }


    public String getOrderId() {
        return orderId;
    }

    public String getFoodName() {
        return foodName;
    }


}
