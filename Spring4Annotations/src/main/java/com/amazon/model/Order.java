package com.amazon.model;

public class Order {

    private Integer orderId;
    private String orderName;
    private Double orderPrice;
    private String email;
    public Order(Integer orderId, String orderName, Double orderPrice , String email) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.email = email;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getOrderName() {
        return orderName;
    }


    public Double getOrderPrice() {
        return orderPrice;
    }

    public String getEmail(){
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", orderPrice=" + orderPrice +
                ", email='" + email + '\'' +
                '}';
    }
}

