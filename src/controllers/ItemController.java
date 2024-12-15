package controllers;

import enums.ItemStatus;
import models.Item;
import modules.Response;
import singleton.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ItemController {

    // Constructor

    public ItemController() {
        database = Database.getInstance();
    }

    // Properties

    private Database database;

    // Methods

    /*
    This method is used to browse available items based on a keyword.
    It returns a response object containing an array list of items.
     */
    public Response<ArrayList<Item>> browseAvailableItems(String keyword) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ? AND LOWER(name) LIKE ?;";
        String status = ItemStatus.APPROVED.toString();
        String keywordPattern = "%" + keyword.toLowerCase() + "%";

        try {
            PreparedStatement statement = database.prepareStatement(query, status, keywordPattern);

            ResultSet resultSet = statement.executeQuery();
            items.addAll(getItemsFromResultSet(resultSet));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to browse available items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Available items browsed successfully.",
                items
        );
    }

    /*
    This method is used to browse requested items based on a keyword.
    It returns a response object containing an array list of items.
     */
    public Response<ArrayList<Item>> browseRequestedItems(String keyword) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ? AND LOWER(name) LIKE ?;";
        String status = ItemStatus.PENDING.toString();
        String keywordPattern = "%" + keyword.toLowerCase() + "%";

        try {
            PreparedStatement statement = database.prepareStatement(query, status, keywordPattern);

            ResultSet resultSet = statement.executeQuery();
            items.addAll(getItemsFromResultSet(resultSet));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to browse requested items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Requested items browsed successfully.",
                items
        );
    }

    /*
    This method is used to get existing, available items from the database.
    It returns a response object containing an array list of items.
     */
    public Response<ArrayList<Item>> getAvailableItems() {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ?;";
        String status = ItemStatus.APPROVED.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status);

            ResultSet resultSet = statement.executeQuery();
            items.addAll(getItemsFromResultSet(resultSet));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to get available items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Available items retrieved successfully.",
                items
        );
    }

    /*
    This method is used to get existing, requested items from the database.
    It returns a response object containing an array list of items.
     */
    public Response<ArrayList<Item>> getRequestedItems() {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ?;";
        String status = ItemStatus.PENDING.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status);

            ResultSet resultSet = statement.executeQuery();
            items.addAll(getItemsFromResultSet(resultSet));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to get requested items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Requested items retrieved successfully.",
                items
        );
    }

    /*
    This method is used to get a seller's items from the database, based on the seller's ID.
    It returns a response object containing an array list of items.
     */
    public Response<ArrayList<Item>> getSellerItems(String sellerId) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE seller_id = ? AND NOT status = ?;";
        String status = ItemStatus.INVALID.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, sellerId, status);

            ResultSet resultSet = statement.executeQuery();
            items.addAll(getItemsFromResultSet(resultSet));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to get seller items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Seller items retrieved successfully.",
                items
        );
    }

    /*
    This method is used to insert a new item with PENDING status into the database.
    It returns a response object containing the number of rows affected (1+ if successful, 0 if failed).
     */
    public Response<Integer> uploadItem(String sellerId, String name, String size, String price, String category) {
        String errorMessage = validateItem(name, category, size, price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n" + errorMessage,
                    0);
        }

        int rowsAffected = 0;

        Item latestItem = getLatestItemFromDatabase();

        int latestNumber = 0;
        if (latestItem != null) {
            // Ex. latestItem.getId() = "IID0005", substring(3) = "0005"
            latestNumber = Integer.parseInt(latestItem.getId().substring(3));
        }

        String query = "INSERT INTO items (id, seller_id, name, size, price, category, status, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        String id = String.format("IID%04d", latestNumber + 1);
        double priceNumber = Double.parseDouble(price);
        String status = ItemStatus.PENDING.toString();
        String note = "";

        try {
            PreparedStatement statement = database.prepareStatement(query, id, sellerId, name, size, priceNumber, category, status, note);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Item uploaded successfully and is pending approval." : "Failed to upload item.",
                rowsAffected
        );
    }

    /*
    This method is used to update an existing item in the database, depending on the details provided.
    It returns a response object containing the number of rows affected (1+ if successful, 0 if failed).
     */
    public Response<Integer> updateItem(String id, String name, String size, String price, String category) {
        String errorMessage = validateItem(name, category, size, price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n" + errorMessage,
                    0);
        }

        int rowsAffected = 0;

        String query = "UPDATE items SET name = ?, size = ?, price = ?, category = ? WHERE id = ?;";
        double priceNumber = Double.parseDouble(price);

        try {
            PreparedStatement statement = database.prepareStatement(query, name, size, priceNumber, category, id);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to update item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Item updated successfully." : "Failed to update item.",
                rowsAffected
        );
    }

    /*
    This method is used to sell an item by updating its status to SOLD_OUT in the database.
    It returns a response object containing the number of rows affected (1+ if successful, 0 if failed).
     */
    public Response<Integer> sellItem(String id) {
        int rowsAffected = 0;

        String query = "UPDATE items SET status = ? WHERE id = ?;";
        String status = ItemStatus.SOLD_OUT.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status, id);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to sell item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Item sold out successfully." : "Failed to sell item.",
                rowsAffected
        );
    }

    /*
    This method is used to delete an item by updating its status to INVALID in the database.
    It returns a response object containing the number of rows affected (1+ if successful, 0 if failed).
     */
    public Response<Integer> invalidateItem(String id) {
        int rowsAffected = 0;

        String query = "UPDATE items SET status = ? WHERE id = ?;";
        String status = ItemStatus.INVALID.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status, id);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to invalidate item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Item invalidated successfully." : "Failed to invalidate item.",
                rowsAffected
        );
    }

    /*
    This method is used to approve an item by updating its status to APPROVED in the database.
    It returns a response object containing the number of rows affected (1+ if successful, 0 if failed).
     */
    public Response<Integer> approveItem(String id) {
        int rowsAffected = 0;

        String query = "UPDATE items SET status = ? WHERE id = ?;";
        String status = ItemStatus.APPROVED.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status, id);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to approve item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Item approved successfully." : "Failed to approve item.",
                rowsAffected
        );
    }

    /*
    This method is used to decline an item by updating its status to DECLINED in the database.
    It returns a response object containing the number of rows affected (1+ if successful, 0 if failed).
     */
    public Response<Integer> declineItem(String id, String reason) {
        String errorMessage = validateReason(reason);

        if (!errorMessage.isEmpty()) {
            return new Response<>(false, "Failed to decline item:\r\n" + errorMessage, 0);
        }

        int rowsAffected = 0;

        String query = "UPDATE items SET status = ?, note = ? WHERE id = ?;";
        String status = ItemStatus.DECLINED.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status, reason, id);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to decline item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Item declined successfully." : "Failed to decline item.",
                rowsAffected
        );
    }

    // Utilities

    /*
    This method is used to get items from a result set.
    It returns an array list of items.
     */
    private ArrayList<Item> getItemsFromResultSet(ResultSet resultSet) {
        ArrayList<Item> items = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Item item = new Item(
                    resultSet.getString("id"),
                    resultSet.getString("seller_id"),
                    resultSet.getString("name"),
                    resultSet.getString("size"),
                    resultSet.getDouble("price"),
                    resultSet.getString("category"),
                    ItemStatus.valueOf(resultSet.getString("status")),
                    resultSet.getString("note")
                );

                items.add(item);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return items;
    }

    /*
    This method is used to get the latest item from the database.
    This works by getting the item with the highest ID number (on a descending list).
    It returns an item.
     */
    private Item getLatestItemFromDatabase() {
        Item item = null;

        String query = "SELECT * FROM items ORDER BY id DESC LIMIT 1;";

        try {
            PreparedStatement statement = database.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                item = new Item(
                    resultSet.getString("id"),
                    resultSet.getString("seller_id"),
                    resultSet.getString("name"),
                    resultSet.getString("size"),
                    resultSet.getDouble("price"),
                    resultSet.getString("category"),
                    ItemStatus.valueOf(resultSet.getString("status")),
                    resultSet.getString("note")
                );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return item;
    }

    // Validations

    /*
    This method is used to validate a string on whether it is empty or not.
    It returns an error message if the validation fails.
     */
    private String validateReason(String reason) {
        String errorMessage = "";

        if (reason.isEmpty()) {
            errorMessage += "- Reason cannot be empty.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate an item based on its name, category, size, and price.
    It returns an error message if any of the validations fail.
     */
    private String validateItem(String name, String category, String size, String price) {
        String errorMessage = "";

        errorMessage += validateName(name);
        errorMessage += validateCategory(category);
        errorMessage += validateSize(size);
        errorMessage += validatePrice(price);

        return errorMessage;
    }

    /*
    This method is used to validate an item's name on whether it is empty or less than 3 characters long.
    It returns an error message if the validation fails.
     */
    private String validateName(String name) {
        String errorMessage = "";

        if (name.isEmpty()) {
            errorMessage += "- Name cannot be empty.\r\n";
        }

        if (name.length() < 3) {
            errorMessage += "- Name must be at least 3 characters long.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate an item's category on whether it is empty or less than 3 characters long.
    It returns an error message if the validation fails.
     */
    private String validateCategory(String category) {
        String errorMessage = "";

        if (category.isEmpty()) {
            errorMessage += "- Category cannot be empty.\r\n";
        }

        if (category.length() < 3) {
            errorMessage += "- Category must be at least 3 characters long.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate an item's size on whether it is empty.
    It returns an error message if the validation fails.
     */
    private String validateSize(String size) {
        String errorMessage = "";

        if (size.isEmpty()) {
            errorMessage += "- Size cannot be empty.\r\n";
        }

        return errorMessage;
    }

    /*
    This method is used to validate an item's price on whether it is empty, less than or equal to 0, or not a valid number.
    It returns an error message if the validation fails.
     */
    private String validatePrice(String price) {
        String errorMessage = "";

        if (price.isEmpty()) {
            errorMessage += "- Price cannot be empty.\r\n";
        }

        try {
            double value = Double.parseDouble(price);

            if (value <= 0) {
                errorMessage += "- Price must be greater than 0.\r\n";
            }
        }
        catch (NumberFormatException ex) {
            errorMessage += "- Price must be a valid number.\r\n";
        }

        return errorMessage;
    }

}
