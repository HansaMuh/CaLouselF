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

    // Designed for: Browse Item feature
    // This method is used to browse through available items that contains the keyword in their name.
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

    // Designed for: Browse Item feature
    // This method is used to browse through available items that contains the keyword in their name.
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

    // Designed for: View Item feature
    // Available items are items that have been approved by the Admin and are still available for purchase from the
    // list.
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

    // Designed for: View Requested Item feature
    // Requested items are items that have not been approved by the Admin and are still waiting for approval.
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

    public Response<Integer> uploadItem(String sellerId, String name, String size, String price, String category) {
        String errorMessage = validateItem(name, category, size, price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n" + errorMessage,
                    null
            );
        }

        int rowsAffected = 0;

        Item latestItem = getLatestItemFromDatabase();
        int latestId = Integer.parseInt(latestItem != null ? latestItem.getId().substring(3) : "0000");

        String query = "INSERT INTO items (id, seller_id, name, size, price, category, status, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        String id = String.format("IID%04d", latestId + 1);
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

    public Response<Integer> updateItem(String id, String name, String size, String price, String category) {
        String errorMessage = validateItem(name, category, size, price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n" + errorMessage,
                    null
            );
        }

        int rowsAffected = 0;

        // Update four fields: name, size, price, and category
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

    public Response<Integer> sellItem(String id) {
        int rowsAffected = 0;

        // Turn the item's status to SOLD_OUT
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

    public Response<Integer> invalidateItem(String id) {
        int rowsAffected = 0;

        // Turn the item's status to INVALID
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

    // Designed for: Approve Item feature
    // This method is used to approve an item that has been requested by a seller.
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

    public Response<Integer> declineItem(String id, String reason) {
        String errorMessage = validateReason(reason);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to decline item:\r\n" + errorMessage,
                    0
            );
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

    private Item getLatestItemFromDatabase() {
        Item item = null;

        String query = "SELECT i.id FROM items AS i ORDER BY id DESC LIMIT 1;";

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

    private String validateReason(String reason) {
        String errorMessage = "";

        if (reason.isEmpty()) {
            errorMessage += "- Reason cannot be empty.\r\n";
        }

        return errorMessage;
    }

    private String validateItem(String name, String category, String size, String price) {
        String errorMessage = "";

        errorMessage += validateName(name);
        errorMessage += validateCategory(category);
        errorMessage += validateSize(size);
        errorMessage += validatePrice(price);

        return errorMessage;
    }

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

    private String validateSize(String size) {
        String errorMessage = "";

        if (size.isEmpty()) {
            errorMessage += "- Size cannot be empty.\r\n";
        }

        return errorMessage;
    }

    private String validatePrice(String price) {
        String errorMessage = "";

        if (price.isEmpty()) {
            errorMessage += "- Price cannot be empty.\r\n";
        }

        // Check if price is a valid number
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
