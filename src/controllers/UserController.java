package controllers;

import models.Response;
import models.User;
import singleton.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserController {

    // Methods

    public static Response<User> login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return new Response<>(
                    false,
                    "Login failed: Username and password cannot be empty.",
                    null
            );
        }

        User user = null;

        String query = "SELECT * FROM users WHERE username = ? AND password = ?;";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

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
                        resultSet.getString("role")
                );

                return new Response<>(
                        true,
                        "Login success.",
                        user
                );
            }
        }
        catch (Exception ex) {
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

    public static Response<User> register(String username, String password, String phoneNumber, String address,
                                          String role) {
        Response<String> validationResponse = checkAccountValidation(username, password, phoneNumber, address, role);

        if (!validationResponse.getIsSuccess()) {
            return new Response<>(
                    false,
                    "Registration failed:\r\n" + validationResponse.getOutput(),
                    null
            );
        }

        Response<Integer> userCountResponse = checkUserCount();

        String id = String.format("UID%04d", userCountResponse.getOutput() + 1); // e.g. UID0001
        User user = new User(id, username, password, phoneNumber, address, role);

        String query = "INSERT INTO users (id, username, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getRole());

            statement.execute();
        }
        catch (Exception ex) {
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

    public static Response<String> checkAccountValidation(String username, String password, String phoneNumber,
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

    public static Response<Integer> checkUserCount() {
        String query = "SELECT COUNT(*) AS user_count FROM users;";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Response<>(
                        true,
                        "User count retrieved.",
                        resultSet.getInt("user_count")
                );
            }
        }
        catch (Exception ex) {
            return new Response<>(
                    false,
                    "An error occurred while retrieving user count.",
                    null
            );
        }

        return new Response<>(
                false,
                "User count not found.",
                0
        );
    }

    // Helpers

    private Response<User> authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?;";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        resultSet.getString("role")
                );

                return new Response<>(
                        true,
                        "Authentication success.",
                        user
                );
            }
        }
        catch (Exception ex) {
            return new Response<>(
                    false,
                    "An error occurred while authenticating.",
                    null
            );
        }

        return new Response<>(
                false,
                "Invalid username or password.",
                null
        );
    }

    // Validations

    private static String validateUsername(String username) {
        String errorMessage = "";

        if (username.isEmpty()) {
            errorMessage += "Username cannot be empty.\r\n";
        }

        if (username.length() < 3) {
            errorMessage += "Username must be at least 3 characters long.\r\n";
        }

        if (!isUsernameUnique(username)) {
            errorMessage += "Username already exists.\r\n";
        }

        return errorMessage;
    }

    private static String validatePassword(String password) {
        String errorMessage = "";

        if (password.isEmpty()) {
            errorMessage += "Password cannot be empty.\r\n";
        }

        if (password.length() < 8) {
            errorMessage += "Password must be at least 8 characters long.\r\n";
        }

        if (!containsSpecialCharacter(password)) {
            errorMessage += "Password must include at least one special character (!, @, #, $, %, ^, &, *).\r\n";
        }

        return errorMessage;
    }

    private static String validatePhoneNumber(String phoneNumber) {
        String errorMessage = "";

        if (!isValidPhoneNumber(phoneNumber)) {
            errorMessage += "Phone number must start with +62 and be at least 10 characters long.\r\n";
        }

        return errorMessage;
    }

    private static String validateAddress(String address) {
        String errorMessage = "";

        if (address.isEmpty()) {
            errorMessage += "Address cannot be empty.\r\n";
        }

        return errorMessage;
    }

    private static String validateRole(String role) {
        String errorMessage = "";

        if (role == null || role.isEmpty()) {
            errorMessage += "Role cannot be empty.\r\n";
        }

        return errorMessage;
    }

    private static boolean isUsernameUnique(String username) {
        String query = "SELECT * FROM users WHERE username = ?;";

        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            return !resultSet.next();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private static boolean containsSpecialCharacter(String password) {
        String specialChars = "!@#$%^&*";

        for (char c : password.toCharArray()) {
            if (specialChars.indexOf(c) != -1) {
                return true;
            }
        }

        return false;
    }

    private static boolean isValidPhoneNumber(String phone) {
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
