package models;

import enums.OfferStatus;

import java.util.Date;

/*
    Offer class represents an offer made by a buyer to purchase an item.
 */
public class Offer {

    // Constructor

    public Offer(String id, String userId, String itemId, double price, Date date, OfferStatus status, String reason) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.price = price;
        this.date = date;
        this.status = status;
        this.reason = reason;
    }

    // Properties

    private String id;
    private String userId;
    private String itemId;
    private double price;
    private Date date;
    private OfferStatus status;
    private String reason;

    // Getters

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemId() {
        return itemId;
    }

    public double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

}