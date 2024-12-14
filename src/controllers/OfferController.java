package controllers;

import enums.ItemStatus;
import enums.OfferStatus;
import models.Item;
import models.Offer;
import models.OfferedItem;
import models.Transaction;
import modules.Response;
import singleton.Database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

public class OfferController {

    // Constructor
    public OfferController() {
        database = Database.getInstance();
    }

    // Properties
    private Database database;

    // Methods

    public Response<ArrayList<OfferedItem>> getOfferedItems(String sellerId) {
        ArrayList<OfferedItem> items = new ArrayList<>();

        String query = "SELECT i.*, o.id AS offer_id, o.price AS offered_price FROM offers AS o " +
                "LEFT JOIN items AS i ON o.item_id = i.id " +
                "WHERE o.status = ? AND i.seller_id = ?;";
        String status = OfferStatus.PENDING.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status, sellerId);
            ResultSet resultSet = statement.executeQuery();
            items.addAll(getOfferedItemsFromResultSet(resultSet));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while getting offered items.",
                    items
            );
        }

        return new Response<>(
                true,
                "Offered items retrieved successfully.",
                items
        );
    }

    public Response<Integer> makeOffer(String userId, String itemId, String price) {
        String errorMessage = validatePrice(price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to make an offer:\r\n" + errorMessage,
                    0
            );
        }

        double priceNumber = Double.parseDouble(price);
        String secondErrorMessage = validateOffer(itemId, priceNumber);

        if (!secondErrorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to make an offer:\r\n" + secondErrorMessage,
                    0
            );
        }

        int rowsAffected = 0;

        // Ambil offer terakhir dari database
        Offer latestOffer = getLatestOfferFromDatabase();
        int latestId = 0;
        if (latestOffer != null) {
            latestId = Integer.parseInt(latestOffer.getId().substring(3));
        }

        String offerQuery = "INSERT INTO offers (id, user_id, item_id, price, date, status, reason) VALUES (?, ?, ?, ?, ?, ?, ?);";
        String newId = String.format("OID%04d", latestId + 1);
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        String status = OfferStatus.PENDING.toString();
        String reason = "";

        try {
            PreparedStatement statement = database.prepareStatement(offerQuery, newId, userId, itemId, priceNumber, date, status, reason);
            rowsAffected = statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to make an offer:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Offer made successfully." : "Failed to make an offer.",
                rowsAffected
        );
    }

    public Response<Integer> acceptOffer(String id, String userId, String itemId) {
        int rowsAffected = 0;

        String query = "UPDATE offers SET status = ? WHERE id = ?;";
        String status = OfferStatus.ACCEPTED.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status, id);
            rowsAffected = statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to accept offer:\r\n- " + ex.getMessage(),
                    0
            );
        }

        if (rowsAffected == 0) {
            return new Response<>(
                    false,
                    "Failed to accept offer:\r\n- Offer not found.",
                    0
            );
        }

        // Lanjutkan dengan melakukan purchase
        TransactionController transactionController = new TransactionController();
        Response<Integer> transactionResponse = transactionController.purchaseItem(userId, itemId);

        return new Response<>(
                true,
                "Offer accepted successfully.",
                rowsAffected
        );
    }

    public Response<Integer> declineOffer(String id, String userId, String itemId, String reason) {
        String errorMessage = validateReason(reason);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to decline offer:\r\n" + errorMessage,
                    0
            );
        }

        int rowsAffected = 0;

        String query = "UPDATE offers SET status = ?, reason = ? WHERE id = ?;";
        String status = OfferStatus.DECLINED.toString();

        try {
            PreparedStatement statement = database.prepareStatement(query, status, reason, id);
            rowsAffected = statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to decline offer:\r\n- " + ex.getMessage(),
                    0
            );
        }

        return new Response<>(
                rowsAffected > 0,
                rowsAffected > 0 ? "Offer declined successfully." : "Failed to decline offer.",
                rowsAffected
        );
    }

    // Utilities

    private Offer getLatestOfferFromDatabase() {
        Offer offer = null;

        // Perbaiki query untuk mengambil semua kolom
        String query = "SELECT * FROM offers ORDER BY id DESC LIMIT 1;";

        try {
            PreparedStatement statement = database.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                offer = new Offer(
                        resultSet.getString("id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("item_id"),
                        resultSet.getDouble("price"),
                        resultSet.getDate("date"),
                        OfferStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("reason")
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return offer;
    }

    private Offer getHighestOfferByItem(String itemId) {
        Offer offer = null;

        String query = "SELECT * FROM offers WHERE item_id = ? ORDER BY price DESC LIMIT 1;";

        try {
            PreparedStatement statement = database.prepareStatement(query, itemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                offer = new Offer(
                        resultSet.getString("id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("item_id"),
                        resultSet.getDouble("price"),
                        resultSet.getDate("date"),
                        OfferStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("reason")
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return offer;
    }

    private Item getItemFromDatabase(String itemId) {
        Item item = null;

        String query = "SELECT * FROM items WHERE id = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(query, itemId);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return item;
    }

    private ArrayList<OfferedItem> getOfferedItemsFromResultSet(ResultSet resultSet) {
        ArrayList<OfferedItem> items = new ArrayList<>();

        try {
            while (resultSet.next()) {
                OfferedItem item = new OfferedItem(
                        resultSet.getString("offer_id"),
                        resultSet.getDouble("offered_price"),
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return items;
    }

    // Validations

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
        } catch (NumberFormatException ex) {
            errorMessage += "- Price must be a valid number.\r\n";
        }

        return errorMessage;
    }

    private String validateOffer(String itemId, double price) {
        String errorMessage = "";

        Offer highestOffer = getHighestOfferByItem(itemId);

        if (highestOffer != null && price < highestOffer.getPrice()) {
            errorMessage += "- Offer must be higher than the current highest offer that is " + highestOffer.getPrice() + ".\r\n";
        } else if (highestOffer == null) {
            Item item = getItemFromDatabase(itemId);

            if (item != null && price > item.getPrice()) {
                errorMessage += "- Offer must be lower than the item price that is " + item.getPrice() + ".\r\n";
            }
        }

        return errorMessage;
    }

    private String validateReason(String reason) {
        String errorMessage = "";
        if (reason.isEmpty()) {
            errorMessage += "- Reason cannot be empty.\r\n";
        }
        return errorMessage;
    }

}
