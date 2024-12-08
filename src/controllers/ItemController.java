package controllers;

import enums.ItemStatus;
import models.Item;
import modules.Response;
import singleton.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ItemController {

    // Methods

    // Designed for: Browse Item feature
    // This method is used to browse through available items that contains the keyword in their name.
    public static Response<ArrayList<Item>> browseAvailableItems(String keyword) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ? AND LOWER(name) LIKE ?;";
        String status = ItemStatus.APPROVED.toString();

        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

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
    public static Response<ArrayList<Item>> browseRequestedItems(String keyword) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ? AND LOWER(name) LIKE ?;";
        String status = ItemStatus.PENDING.toString();

        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

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
    public static Response<ArrayList<Item>> getAvailableItems() {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ?;";
        String status = ItemStatus.APPROVED.toString();

        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

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
    public static Response<ArrayList<Item>> getRequestedItems() {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT * FROM items WHERE status = ?;";
        String status = ItemStatus.PENDING.toString();

        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

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
    public static Response<Item> approveItem(String id) {
        String query = "UPDATE items SET status = ? WHERE id = ?;";
        String status = ItemStatus.APPROVED.toString();

        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, status);
            statement.setString(2, id);

            statement.execute();
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

    public static Response<Item> declineItem(String id, String reason) {
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
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, status);
            statement.setString(2, reason);
            statement.setString(3, id);

            statement.execute();
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

    // Validations

    private static String validateReason(String reason) {
        String errorMessage = "";

        if (reason.isEmpty()) {
            errorMessage += "- Reason cannot be empty.\r\n";
        }

        return errorMessage;
    }

}
