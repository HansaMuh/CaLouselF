package controllers;

import enums.ItemStatus;
import models.Item;
import modules.Response;
import singleton.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TransactionController {

    // Methods

    public static Response<ArrayList<Item>> getItemsByTransaction(String userId) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT t.id AS transaction_id, i.* FROM transactions AS t LEFT JOIN items AS i ON t.item_id = i.id WHERE t.user_id = ?;";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Item item = new Item(
                        resultSet.getString("transaction_id"),
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
                    "Failed to get items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Items retrieved successfully.",
                items
        );
    }

}
