package Models;

import java.sql.Date;

public class Order {
    private int orderID;
    private Date orderDate;
    private int customerID;
    private int foodID;
    private int quantity;
    private String tableId;
    private String status;

    public Order() {
    }

    public Order(int orderID, Date orderDate, int customerID, int foodID, int quantity, String tableId, String status) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.customerID = customerID;
        this.foodID = foodID;
        this.quantity = quantity;
        this.tableId = tableId;
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
