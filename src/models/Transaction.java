package models;

import java.util.Date;

public class Transaction {

    // Constructor

    public Transaction(String id, String userId, String itemId, Date date) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.date = date;
    }

    // Properties

    private String id;
    private String userId;
    private String itemId;
    private Date date;

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

    public Date getDate() {
        return date;
    }

}