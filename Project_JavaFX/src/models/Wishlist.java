package models;

//File: models/Wishlist.java

public class Wishlist {
 private int id;
 private int userId;
 private int itemId;

 // Constructors
 public Wishlist() {}

 public Wishlist(int userId, int itemId) {
     this.userId = userId;
     this.itemId = itemId;
 }

 // Getters and Setters
 public int getId() { return id; }
 public void setId(int id) { this.id = id; }

 public int getUserId() { return userId; }
 public void setUserId(int userId) { this.userId = userId; }

 public int getItemId() { return itemId; }
 public void setItemId(int itemId) { this.itemId = itemId; }
}
