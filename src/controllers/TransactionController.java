package controllers;

import enums.ItemStatus;
import models.Item;
import models.Transaction;
import modules.Response;
import singleton.Database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

public class TransactionController {

    // Constructor
    
    public TransactionController() {
        database = Database.getInstance();
    }
    
    // Properties
    
    private Database database;
    
    // Methods

    public Response<ArrayList<Item>> getItemsByTransaction(String userId) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT t.id AS transaction_id, i.* FROM transactions AS t LEFT JOIN items AS i ON t.item_id = i.id WHERE t.user_id = ?;";
        try {
            PreparedStatement statement = database.prepareStatement(query);

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

    public Response<Transaction> purchaseItem(String userId, String itemId) {
        Response<String> response = checkLatestTransactionId();
        if (!response.getIsSuccess()) {
            return new Response<>(
                    false,
                    "Failed to purchase item:\r\n- " + response.getMessage(),
                    null
            );
        }

        int latestId = Integer.parseInt(response.getOutput().substring(3));
        String id = String.format("TID%04d", latestId + 1);

        Transaction transaction = new Transaction(id, userId, itemId, Calendar.getInstance().getTime());

        String transactionQuery = "INSERT INTO transactions (id, user_id, item_id, date) VALUES (?, ?, ?, ?);";
        String wishlistsQuery = "DELETE FROM wishlists WHERE item_id = ?;";
        String itemQuery = "UPDATE items SET status = ? WHERE id = ?;";
        try {
            PreparedStatement transactionStatement = database.prepareStatement(transactionQuery);

            transactionStatement.setString(1, transaction.getId());
            transactionStatement.setString(2, transaction.getUserId());
            transactionStatement.setString(3, transaction.getItemId());
            transactionStatement.setDate(4, new Date(transaction.getDate().getTime()));

            transactionStatement.executeUpdate();

            PreparedStatement wishlistsStatement = database.prepareStatement(wishlistsQuery);

            wishlistsStatement.setString(1, transaction.getItemId());

            wishlistsStatement.executeUpdate();

            PreparedStatement itemStatement = database.prepareStatement(itemQuery);

            itemStatement.setString(1, ItemStatus.SOLD_OUT.toString());
            itemStatement.setString(2, transaction.getItemId());

            itemStatement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to purchase item:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Item purchased successfully.",
                transaction
        );
    }

    public Response<String> checkLatestTransactionId() {
        String transactionId = "TID0000";

        String query = "SELECT t.id FROM transactions AS t ORDER BY id DESC LIMIT 1;";
        try {
            PreparedStatement statement = database.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                transactionId = resultSet.getString("id");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while checking the latest transaction ID.",
                    transactionId
            );
        }

        return new Response<>(
                true,
                "Latest transaction ID checked successfully.",
                transactionId
        );
    }

}
