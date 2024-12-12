package controllers;

import enums.ItemStatus;
import models.Item;
import models.Wishlist;
import modules.Response;
import singleton.Database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

public class WishlistController {

    // Constructor
    
    public WishlistController() {
        database = Database.getInstance();
    }
    
    // Properties
    
    private Database database;

    // Methods

    public Response<ArrayList<Item>> getWishlistedItems(String userId) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT i.* FROM wishlists AS w LEFT JOIN items AS i ON w.item_id = i.id WHERE w.user_id = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(query, userId);

            ResultSet resultSet = statement.executeQuery();
            items.addAll(getItemsFromResultSet(resultSet));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to get wishlisted items:\r\n- " + ex.getMessage(),
                    items
            );
        }

        return new Response<>(
                true,
                "Wishlisted items retrieved successfully.",
                items
        );
    }

    public Response<Integer> addWishlist(String itemId, String userId) {
        int rowsAffected = 0;

        Wishlist latestWishlist = getLatestWishlistFromDatabase();
        int latestId = Integer.parseInt(latestWishlist != null ? latestWishlist.getId().substring(3) : "0000");

        String query = "INSERT INTO wishlists (id, item_id, user_id, date) VALUES (?, ?, ?, ?);";
        String id = String.format("WID%04d", latestId + 1);
        Date date = new Date(Calendar.getInstance().getTime().getTime());

        try {
            PreparedStatement statement = database.prepareStatement(query, id, itemId, userId, date);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to add wishlist:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Wishlist added successfully." : "No wishlist added.",
                rowsAffected
        );
    }

    public Response<Integer> removeWishlistsByItem(String itemId) {
        int rowsAffected = 0;

        String query = "DELETE FROM wishlists WHERE item_id = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(query, itemId);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to remove wishlists by item:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Wishlists removed successfully." : "No wishlists removed.",
                rowsAffected
        );
    }

    public Response<Integer> removeWishlistsByUser(String itemId, String userId) {
        int rowsAffected = 0;

        String query = "DELETE FROM wishlists WHERE item_id = ? AND user_id = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(query, itemId, userId);

            rowsAffected = statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to remove wishlists by user:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Wishlists removed successfully." : "No wishlists removed.",
                rowsAffected
        );
    }

    // Utilities

    public ArrayList<Item> getItemsFromResultSet(ResultSet resultSet) {
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

    public Wishlist getLatestWishlistFromDatabase() {
        Wishlist wishlist = null;

        String query = "SELECT * FROM wishlists ORDER BY id DESC LIMIT 1;";

        try {
            PreparedStatement statement = database.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                wishlist = new Wishlist(
                        resultSet.getString("id"),
                        resultSet.getString("item_id"),
                        resultSet.getString("user_id"),
                        resultSet.getDate("date")
                );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return wishlist;
    }

}

