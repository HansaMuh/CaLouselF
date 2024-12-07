package models;

//File: models/User.java

public class User {
 private int id;
 private String username;
 private String password;
 private String phoneNumber;
 private String address;
 private String role;

 // Constructors
 public User() {}

 public User(String username, String password, String phoneNumber, String address, String role) {
     this.username = username;
     this.password = password;
     this.phoneNumber = phoneNumber;
     this.address = address;
     this.role = role;
 }

 // Getters and Setters
 public int getId() { return id; }
 public void setId(int id) { this.id = id; }

 public String getUsername() { return username; }
 public void setUsername(String username) { this.username = username; }

 public String getPassword() { return password; }
 public void setPassword(String password) { this.password = password; }

 public String getPhoneNumber() { return phoneNumber; }
 public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

 public String getAddress() { return address; }
 public void setAddress(String address) { this.address = address; }

 public String getRole() { return role; }
 public void setRole(String role) { this.role = role; }
}
