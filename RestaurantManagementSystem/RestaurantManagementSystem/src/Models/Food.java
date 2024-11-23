package Models;

public class Food {
    private int foodID;
    private String name;
    private float price;

    public Food() {
    }

    public Food(int foodID, String name, float price) {
        this.foodID = foodID;
        this.name = name;
        this.price = price;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodID=" + foodID +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
