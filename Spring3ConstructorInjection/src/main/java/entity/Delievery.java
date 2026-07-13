package entity;

public class Delievery {

    private String eta;
    private String delieveryStatus;


    public Delievery(String eta , String delieveryStatus){
        this.eta = eta;
        this.delieveryStatus= delieveryStatus;
    }


    public String getEta() {
        return eta;
    }

    public String getDelieveryStatus() {
        return delieveryStatus;
    }

    @Override
    public String toString() {
        return "Delievery{" +
                "eta='" + eta + '\'' +
                ", delieveryStatus='" + delieveryStatus + '\'' +
                '}';
    }
}
