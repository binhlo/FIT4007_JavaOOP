package Models;

import java.sql.Date;

public class Reservation {
    private int reservationID;
    private int customerID;
    private Date reservationDate;
    private int tableNumber;
    private int numberOfGuests;
    private boolean cancelled;

    public Reservation() {
    }

    public Reservation(int reservationID, int customerID, Date reservationDate, int tableNumber, int numberOfGuests, boolean cancelled) {
        this.reservationID = reservationID;
        this.customerID = customerID;
        this.reservationDate = reservationDate;
        this.tableNumber = tableNumber;
        this.numberOfGuests = numberOfGuests;
        this.cancelled = cancelled;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
