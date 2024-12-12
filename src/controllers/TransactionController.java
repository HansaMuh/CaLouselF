package controllers;

import enums.ItemStatus;
import models.Item;
import models.Transaction;
import models.TransactionalItem;
import models.Wishlist;
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

    public Response<ArrayList<TransactionalItem>> getItemsByTransaction(String userId) {
        ArrayList<TransactionalItem> items = new ArrayList<>();

        String query = "SELECT t.id AS transaction_id, i.* FROM transactions AS t LEFT JOIN items AS i ON t.item_id = i.id WHERE t.user_id = ?;";
        try {
            PreparedStatement statement = database.prepareStatement(query, userId);

            ResultSet resultSet = statement.executeQuery();
            items.addAll(getTransactionalItemsFromResultSet(resultSet));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to get transactional items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Transactional items retrieved successfully.",
                items
        );
    }

    public Response<Integer> purchaseItem(String userId, String itemId) {
        int rowsAffected = 0;

        Transaction latestTransaction = getLatestTransactionFromDatabase();
        int latestId = Integer.parseInt(latestTransaction != null ? latestTransaction.getId().substring(3) : "0000");

        String query = "INSERT INTO transactions (id, user_id, item_id, date) VALUES (?, ?, ?, ?);";
        String id = String.format("TID%04d", latestId + 1);
        Date date = new Date(Calendar.getInstance().getTime().getTime());

        try {
            PreparedStatement statement = database.prepareStatement(query, id, userId, itemId, date);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to purchase item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        WishlistController wishlistController = new WishlistController();
        wishlistController.removeWishlistsByItem(itemId);

        ItemController itemController = new ItemController();
        itemController.sellItem(itemId);

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Item purchased successfully." : "Failed to purchase item.",
                rowsAffected
        );
    }

    // Utilities

    public Transaction getLatestTransactionFromDatabase() {
        Transaction transaction = null;

        String query = "SELECT * FROM transactions ORDER BY id DESC LIMIT 1;";

        try {
            PreparedStatement statement = database.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                transaction = new Transaction(
                        resultSet.getString("id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("item_id"),
                        resultSet.getDate("date")
                );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return transaction;
    }

    public ArrayList<TransactionalItem> getTransactionalItemsFromResultSet(ResultSet resultSet) {
        ArrayList<TransactionalItem> items = new ArrayList<>();

        try {
            while (resultSet.next()) {
                TransactionalItem item = new TransactionalItem(
                        resultSet.getString("transaction_id"),
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

}
