package models;

import java.util.Date;

public class Wishlist {

    // Constructor

    public Wishlist(String id, String itemId, String userId, Date date) {
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
        this.date = date;
    }

    // Properties

    private String id;
    private String itemId;
    private String userId;
    private Date date;

    // Getters

    public String getId() {
        return id;
    }

    public String getItemId() {
        return itemId;
    }

    public String getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

}