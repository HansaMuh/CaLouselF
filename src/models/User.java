package models;

public class User {

    // Constructor

    public User(String id, String username, String password, String phoneNumber, String address, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    // Properties

    private String id;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private String role;

    // Getters

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

}