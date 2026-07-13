package entity;

public class Kitchen {

    private String foodId;
    private String foodItem;
    private String foodStatus;

    public Kitchen(String foodStatus , String foodItem , String foodId){
        this.foodStatus =foodStatus;
        this.foodItem = foodItem;
        this.foodId = foodId;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodStatus() {
        return foodStatus;
    }


    @Override
    public String toString() {
        return "Kitchen{" +
                "foodId='" + foodId + '\'' +
                ", foodItem='" + foodItem + '\'' +
                ", foodStatus='" + foodStatus + '\'' +
                '}';
    }
}
