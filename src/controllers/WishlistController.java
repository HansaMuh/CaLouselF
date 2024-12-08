package controllers;

import enums.ItemStatus;
import enums.UserRole;
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

    // Methods

    public static Response<ArrayList<Item>> getWishlistedItems(String userId) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT i.* FROM wishlists AS w LEFT JOIN items AS i ON w.item_id = i.id WHERE w.user_id = ?;";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, userId);

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

    public static Response<Wishlist> addWishlist(String itemId, String userId) {
        Response<String> latestWishlistIdResponse = checkLatestWishlistId();

        int idNumber = Integer.parseInt(latestWishlistIdResponse.getOutput().substring(3));
        String id = String.format("WID%04d", idNumber + 1);

        Wishlist wishlist = new Wishlist(
                id,
                itemId,
                userId,
                Calendar.getInstance().getTime()
        );

        String query = "INSERT INTO wishlists (id, item_id, user_id, date) VALUES (?, ?, ?, ?);";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, wishlist.getId());
            statement.setString(2, wishlist.getItemId());
            statement.setString(3, wishlist.getUserId());
            statement.setDate(4, new Date(wishlist.getDate().getTime()));

            statement.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to add wishlist:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Wishlist added successfully.",
                wishlist
        );
    }

    public static Response<Wishlist> removeWishlist(String itemId, String userId) {
        String query = "DELETE FROM wishlists WHERE item_id = ? AND user_id = ?;";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);

            statement.setString(1, itemId);
            statement.setString(2, userId);

            statement.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to remove wishlist:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Wishlist removed successfully.",
                null
        );
    }

    public static Response<String> checkLatestWishlistId() {
        String wishlistId = "WID0000";

        String query = "SELECT w.id FROM wishlists AS w ORDER BY id DESC LIMIT 1;";
        try {
            PreparedStatement statement = Database.getInstance().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                wishlistId = resultSet.getString("id");
            }
        }
        catch (Exception ex) {
            return new Response<>(
                    false,
                    "An error occurred while retrieving latest wishlist ID.",
                    null
            );
        }

        return new Response<>(
                true,
                "Latest wishlist ID retrieved.",
                wishlistId
        );
    }

}

