package models;

//File: models/Item.java


public class Item {
 private int id;
 private String itemName;
 private String category;
 private String size;
 private double price;
 private int sellerId;
 private String status;

 // Constructors
 public Item() {}

 public Item(String itemName, String category, String size, double price, int sellerId) {
     this.itemName = itemName;
     this.category = category;
     this.size = size;
     this.price = price;
     this.sellerId = sellerId;
     this.status = "Pending";
 }

 // Getters and Setters
 public int getId() { return id; }
 public void setId(int id) { this.id = id; }

 public String getItemName() { return itemName; }
 public void setItemName(String itemName) { this.itemName = itemName; }

 public String getCategory() { return category; }
 public void setCategory(String category) { this.category = category; }

 public String getSize() { return size; }
 public void setSize(String size) { this.size = size; }

 public double getPrice() { return price; }
 public void setPrice(double price) { this.price = price; }

 public int getSellerId() { return sellerId; }
 public void setSellerId(int sellerId) { this.sellerId = sellerId; }

 public String getStatus() { return status; }
 public void setStatus(String status) { this.status = status; }
}

