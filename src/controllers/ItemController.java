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
                        ItemStatus.valueOf(resultSet.getString("status"))
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
                        ItemStatus.valueOf(resultSet.getString("status"))
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
}
