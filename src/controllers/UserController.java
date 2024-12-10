package controllers;

import enums.UserRole;
import modules.Response;
import models.User;
import singleton.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserController {

    // Constructor
    
    public UserController() {
        database = Database.getInstance();
    }

    // Properties
    
    private Database database;

    // Methods

    public Response<User> login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return new Response<>(
                    false,
                    "Login failed:\r\n- Username and password cannot be empty.",
                    null
            );
        }

        if (username.equals("admin") && password.equals("admin")) {
            return new Response<>(
                    true,
                    "Login success.",
                    new User(
                            "UID0000",
                            username,
                            password,
                            "",
                            "",
                            UserRole.ADMIN)
            );
        }

        User user = null;

        String query = "SELECT * FROM users WHERE username = ? AND password = ?;";
        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);

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

                return new Response<>(
                        true,
                        "Login success.",
                        user
                );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while logging in.",
                    null
            );
        }

        return new Response<>(
                false,
                "Invalid username or password.",
                null
        );
    }

    public Response<User> register(String username, String password, String phoneNumber, String address,
                                          String role) {
        Response<String> validationResponse = checkAccountValidation(username, password, phoneNumber, address, role);

        if (!validationResponse.getIsSuccess()) {
            return new Response<>(
                    false,
                    "Registration failed:\r\n" + validationResponse.getOutput(),
                    null
            );
        }

        Response<String> latestUserIdResponse = checkLatestUserId();
        if (!latestUserIdResponse.getIsSuccess()) {
            return new Response<>(
                    false,
                    "An error occurred while registering.",
                    null
            );
        }

        int idNumber = Integer.parseInt(latestUserIdResponse.getOutput().substring(3));
        String id = String.format("UID%04d", idNumber + 1); // e.g. UID0001
        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        User user = new User(id, username, password, phoneNumber, address, userRole);

        String query = "INSERT INTO users (id, username, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getRole().toString());

            statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while saving the user.",
                    null
            );
        }

        return new Response<>(
                true,
                "Registration success.",
                user
        );
    }

    public Response<String> checkAccountValidation(String username, String password, String phoneNumber,
                                                          String address, String role) {
        String errorMessage = "";

        errorMessage += validateUsername(username);
        errorMessage += validatePassword(password);
        errorMessage += validatePhoneNumber(phoneNumber);
        errorMessage += validateAddress(address);
        errorMessage += validateRole(role);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Validation failed.",
                    errorMessage);
        }
        else {
            return new Response<>(
                    true,
                    "Validation success.",
                    null
            );
        }
    }

    public Response<String> checkLatestUserId() {
        String userId = "UID0000";

        String query = "SELECT u.id FROM users AS u ORDER BY id DESC LIMIT 1;";
        try {
            PreparedStatement statement = database.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getString("id");
            }
        }
        catch (Exception ex) {
            return new Response<>(
                    false,
                    "An error occurred while retrieving latest user ID.",
                    null
            );
        }

        return new Response<>(
                true,
                "Latest user ID retrieved.",
                userId
        );
    }

    // Validations

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

    private String validatePhoneNumber(String phoneNumber) {
        String errorMessage = "";

        if (!isValidPhoneNumber(phoneNumber)) {
            errorMessage += "- Phone number must start with +62 and be at least 10 characters long.\r\n";
        }

        return errorMessage;
    }

    private String validateAddress(String address) {
        String errorMessage = "";

        if (address.isEmpty()) {
            errorMessage += "- Address cannot be empty.\r\n";
        }

        return errorMessage;
    }

    private String validateRole(String role) {
        String errorMessage = "";

        if (role == null || role.isEmpty()) {
            errorMessage += "- Role cannot be empty.\r\n";
        }

        return errorMessage;
    }

    private boolean isUsernameUnique(String username) {
        String query = "SELECT * FROM users WHERE username = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            return !resultSet.next();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private boolean containsSpecialCharacter(String password) {
        String specialChars = "!@#$%^&*";

        for (char c : password.toCharArray()) {
            if (specialChars.indexOf(c) != -1) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidPhoneNumber(String phone) {
        if (!phone.startsWith("+62") || phone.length() < 10) {
            return false;
        }

        String numberPart = phone.substring(3);

        try {
            Long.parseLong(numberPart);
            return numberPart.length() >= 8; // total panjang termasuk +62 >=10
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
