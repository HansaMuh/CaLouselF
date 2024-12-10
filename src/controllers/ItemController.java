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

        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, keyword.toLowerCase());
            statement.setString(2, status);

            ResultSet resultSet = statement.executeQuery();

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
            return new Response<>(
                    false,
                    "Failed to browse items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Items browsed successfully.",
                items
        );
    }

    // Designed for: Browse Item feature
    // This method is used to browse through available items that contains the keyword in their name.
    public Response<ArrayList<Item>> browseRequestedItems(String keyword) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ? AND LOWER(name) LIKE ?;";
        String status = ItemStatus.PENDING.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, keyword.toLowerCase());
            statement.setString(2, status);

            ResultSet resultSet = statement.executeQuery();

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
            return new Response<>(
                    false,
                    "Failed to browse items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Items browsed successfully.",
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
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, status);

            ResultSet resultSet = statement.executeQuery();

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
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, status);

            ResultSet resultSet = statement.executeQuery();

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

    // Designed for: Approve Item feature
    // This method is used to approve an item that has been requested by a seller.
    public Response<Item> approveItem(String id) {
        String query = "UPDATE items SET status = ? WHERE id = ?;";
        String status = ItemStatus.APPROVED.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, status);
            statement.setString(2, id);

            statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to approve item:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Item approved successfully.",
                null
        );
    }

    public Response<Item> declineItem(String id, String reason) {
        String errorMessage = validateReason(reason);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to decline item:\r\n" + errorMessage,
                    null
            );
        }

        String query = "UPDATE items SET status = ?, note = ? WHERE id = ?;";
        String status = ItemStatus.DECLINED.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, status);
            statement.setString(2, reason);
            statement.setString(3, id);

            statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to decline item:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Item declined successfully.",
                null
        );
    }

    public Response<Item> uploadItem(String sellerId, String name, String size, String price, String category) {
        String errorMessage = validateItem(name, category, size, price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n" + errorMessage,
                    null
            );
        }

        Response<String> response = checkLatestItemId();
        if (!response.getIsSuccess()) {
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n- " + response.getMessage(),
                    null
            );
        }

        int latestId = Integer.parseInt(response.getOutput().substring(3));
        String id = String.format("IID%04d", latestId + 1);

        double priceNumber = Double.parseDouble(price);

        Item item = new Item(id, sellerId, name, size, priceNumber, category, ItemStatus.PENDING, "");

        String query = "INSERT INTO items (id, seller_id, name, size, price, category, status, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, item.getId());
            statement.setString(2, item.getSellerId());
            statement.setString(3, item.getName());
            statement.setString(4, item.getSize());
            statement.setDouble(5, item.getPrice());
            statement.setString(6, item.getCategory());
            statement.setString(7, item.getStatus().toString());
            statement.setString(8, item.getNote());

            statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Item uploaded successfully and is pending approval.",
                item
        );
    }

    public Response<Item> updateItem(String id, String sellerId, String name, String size, String price,
                                     String category) {
        String errorMessage = validateItem(name, category, size, price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to upload item:\r\n" + errorMessage,
                    null
            );
        }

        double priceNumber = Double.parseDouble(price);

        Item item = new Item(id, sellerId, name, size, priceNumber, category, ItemStatus.PENDING, "");

        // Update four fields: name, size, price, and category
        String query = "UPDATE items SET name = ?, size = ?, price = ?, category = ? WHERE id = ? AND seller_id = ?;";
        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, item.getName());
            statement.setString(2, item.getSize());
            statement.setDouble(3, item.getPrice());
            statement.setString(4, item.getCategory());
            statement.setString(5, item.getId());
            statement.setString(6, item.getSellerId());

            statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to update item:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Item updated successfully.",
                item
        );
    }

    public Response<Item> deleteItem(String id, String sellerId) {
        // Turn the item's status to INVALID
        String query = "UPDATE items SET status = ? WHERE id = ? AND seller_id = ? AND status = ?;";
        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, ItemStatus.INVALID.toString());
            statement.setString(2, id);
            statement.setString(3, sellerId);
            statement.setString(4, ItemStatus.APPROVED.toString());

            statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to delete item:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Item deleted successfully.",
                null
        );
    }

    public Response<String> checkLatestItemId() {
        String itemId = "IID0000";

        String query = "SELECT i.id FROM items AS i ORDER BY id DESC LIMIT 1;";
        try {
            PreparedStatement statement = database.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                itemId = resultSet.getString("id");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while checking the latest item.",
                    itemId
            );
        }

        return new Response<>(
                true,
                "Latest item ID checked successfully.",
                itemId
        );
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
