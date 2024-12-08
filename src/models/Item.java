package models;

import enums.ItemStatus;

public class Item {

    // Constructor

    public Item(String id, String sellerId, String name, String size, double price, String category, ItemStatus status, String note)
    {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
        this.status = status;
        this.note = note;
    }

    // Properties

    private String id;
    private String sellerId;
    private String name;
    private String size;
    private double price;
    private String category;
    private ItemStatus status;
    private String note;

    // Getters

    public String getId() {
        return id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

}