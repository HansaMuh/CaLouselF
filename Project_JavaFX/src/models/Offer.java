package models;

//File: models/Offer.java

public class Offer {
 private int id;
 private int userId;
 private int itemId;
 private double offerPrice;
 private String offerDate;

 // Constructors
 public Offer() {}

 public Offer(int userId, int itemId, double offerPrice) {
     this.userId = userId;
     this.itemId = itemId;
     this.offerPrice = offerPrice;
 }

 // Getters and Setters
 public int getId() { return id; }
 public void setId(int id) { this.id = id; }

 public int getUserId() { return userId; }
 public void setUserId(int userId) { this.userId = userId; }

 public int getItemId() { return itemId; }
 public void setItemId(int itemId) { this.itemId = itemId; }

 public double getOfferPrice() { return offerPrice; }
 public void setOfferPrice(double offerPrice) { this.offerPrice = offerPrice; }

 public String getOfferDate() { return offerDate; }
 public void setOfferDate(String offerDate) { this.offerDate = offerDate; }
}
