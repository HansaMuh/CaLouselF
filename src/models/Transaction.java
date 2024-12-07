package models;

//File: models/Transaction.java

public class Transaction {
 private int id;
 private int userId;
 private int itemId;
 private String transactionDate;
 private String itemName;      // Tambahan
 private String category;      // Tambahan
 private String size;          // Tambahan
 private double price;         // Tambahan

 // Constructors
 public Transaction() {}

 public Transaction(int userId, int itemId) {
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

 public String getTransactionDate() { return transactionDate; }
 public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }

 public String getItemName() { return itemName; }
 public void setItemName(String itemName) { this.itemName = itemName; }

 public String getCategory() { return category; }
 public void setCategory(String category) { this.category = category; }

 public String getSize() { return size; }
 public void setSize(String size) { this.size = size; }

 public double getPrice() { return price; }
 public void setPrice(double price) { this.price = price; }
}

