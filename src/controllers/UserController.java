package controllers;

import enums.UserRole;
import models.Item;
import modules.Response;
import models.User;
import singleton.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserController {

    // Constructor
    
    public UserController() {
        database = Database.getInstance();
    }

    // Properties
    
    private Database database;

    // Methods

    /*
    This method is used to get a User from the database based on the given username and password.
    If the username and password are valid, the method will return a Response object containing the User.
     */
    public Response<User> login(String username, String password) {
        String errorMessage = validateLoginInfo(username, password);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Login failed:\r\n" + errorMessage,
                    null
            );
        }

        if (username.equals("admin") && password.equals("admin")) {
            User adminUser = new User(
                    "UID0000",
                    username,
                    password,
                    "",
                    "",
                    UserRole.ADMIN
            );

            return new Response<>(
                    true,
                    "Login success.",
                    adminUser
            );
        }

        User user = null;

        String query = "SELECT * FROM users WHERE username = ? AND password = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(query, username, password);

            ResultSet resultSet = statement.executeQuery();
            user = getUsersFromResultSet(resultSet).get(0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while logging in:\r\n" + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                user != null,
                user != null ? "Login success." : "Login failed:\r\nInvalid username or password.",
                user
        );
    }

    /*
    This method is used to register and insert a new User into the database with the given information.
    If the registration is successful, the method will return a Response object containing the number of rows affected.
    (1+ if successful, 0 if failed)
     */
    public Response<Integer> register(String username, String password, String phoneNumber, String address, String role) {
        String errorMessage = validateAccount(username, password, phoneNumber, address, role);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Registration failed:\r\n" + errorMessage,
                    null
            );
        }

        int rowsAffected = 0;

        User latestUser = getLatestUserFromDatabase();
        int latestId = Integer.parseInt(latestUser != null ? latestUser.getId().substring(3) : "0000");

        String query = "INSERT INTO users (id, username, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?);";
        String id = String.format("UID%04d", latestId + 1);
        String targetRole = role.toUpperCase();

        try {
            PreparedStatement statement = database.prepareStatement(query, id, username, password, phoneNumber, address, targetRole);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while saving the user:\r\n" + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "User saved successfully." : "User failed to save.",
                rowsAffected
        );
    }

    // Utilities

    /*
    This method is used to get the latest User from the database.
    It works by querying the database to get the User with the highest ID number (on a descending list).
    It returns the User object if found, otherwise it returns null.
     */
    private User getLatestUserFromDatabase() {
        User user = null;

        String query = "SELECT * FROM users ORDER BY id DESC LIMIT 1;";

        try {
            PreparedStatement statement = database.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        UserRole.valueOf(resultSet.getString("role"))
                );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return user;
    }

    /*
    This method is used to get all Users from a result set.
    It works by iterating through the result set and creating a User object for each row.
    It returns an array list of Users.
     */
    private ArrayList<User> getUsersFromResultSet(ResultSet resultSet) {
        ArrayList<User> users = new ArrayList<>();

        try {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        UserRole.valueOf(resultSet.getString("role"))
                );

                users.add(user);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return users;
    }

    // Validations

    /*
    This method is used to validate the login information on whether the username and password are empty.
    It returns an error message if the validation fails.
     */
    private String validateLoginInfo(String username, String password) {
        String errorMessage = "";

        if (username.isEmpty()) {
            errorMessage += "- Username cannot be empty.\r\n";
        }

        if (password.isEmpty()) {
            errorMessage += "- Password cannot be empty.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate the account information on whether the details are valid.
    It returns an error message if the validation fails.
     */
    private String validateAccount(String username, String password, String phoneNumber,
                                   String address, String role) {
        String errorMessage = "";

        errorMessage += validateUsername(username);
        errorMessage += validatePassword(password);
        errorMessage += validatePhoneNumber(phoneNumber);
        errorMessage += validateAddress(address);
        errorMessage += validateRole(role);

        return errorMessage;
    }

    /*
    This method is used to validate the username on whether it is empty, less than 3 characters, or already exists.
    It returns an error message if the validation fails.
     */
    private String validateUsername(String username) {
        String errorMessage = "";

        if (username.isEmpty()) {
            errorMessage += "- Username cannot be empty.\r\n";
        }

        if (username.length() < 3) {
            errorMessage += "- Username must be at least 3 characters long.\r\n";
        }

        if (!isUsernameUnique(username)) {
            errorMessage += "- Username already exists.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate the password on whether it is empty, less than 8 characters, or does not
    contain special characters.
    It returns an error message if the validation fails.
     */
    private String validatePassword(String password) {
        String errorMessage = "";

        if (password.isEmpty()) {
            errorMessage += "- Password cannot be empty.\r\n";
        }

        if (password.length() < 8) {
            errorMessage += "- Password must be at least 8 characters long.\r\n";
        }

        if (!containsSpecialCharacter(password)) {
            errorMessage += "- Password must include at least one special character (!, @, #, $, %, ^, &, *).\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate the phone number on whether it starts with +62 and is at least 10 characters long.
    It returns an error message if the validation fails.
     */
    private String validatePhoneNumber(String phoneNumber) {
        String errorMessage = "";

        if (!isValidPhoneNumber(phoneNumber)) {
            errorMessage += "- Phone number must start with +62 and be at least 10 characters long.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate the address on whether it is empty.
    It returns an error message if the validation fails.
     */
    private String validateAddress(String address) {
        String errorMessage = "";

        if (address.isEmpty()) {
            errorMessage += "- Address cannot be empty.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate the role on whether it is empty.
    It returns an error message if the validation fails.
     */
    private String validateRole(String role) {
        String errorMessage = "";

        if (role == null || role.isEmpty()) {
            errorMessage += "- Role cannot be empty.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to check whether the given username is unique in the database.
    It returns true if the username doesn't exist yet, otherwise it returns false.
     */
    private boolean isUsernameUnique(String username) {
        String query = "SELECT * FROM users WHERE username = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(query, username);

            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /*
    This method is used to check whether the given password contains at least one special character.
    It returns true if the password contains a special character, otherwise it returns false.
     */
    private boolean containsSpecialCharacter(String password) {
        String specialChars = "!@#$%^&*";

        for (char c : password.toCharArray()) {
            if (specialChars.indexOf(c) != -1) {
                return true;
            }
        }

        return false;
    }

    /*
    This method is used to check whether the given phone number is valid.
    It returns true if the phone number starts with +62 and is at least 10 characters long, otherwise it returns false.
     */
    private boolean isValidPhoneNumber(String phone) {
        if (!phone.startsWith("+62") || phone.length() < 10) {
            return false;
        }

        String numberPart = phone.substring(3);

        try {
            Long.parseLong(numberPart);
            return numberPart.length() >= 8;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
