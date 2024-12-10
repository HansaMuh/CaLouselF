package controllers;

import enums.ItemStatus;
import enums.OfferStatus;
import models.Item;
import models.Offer;
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

    public Response<Offer> createOffer(String userId, String itemId, String price) {
        String errorMessage = validatePrice(price);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to create offer:\r\n" + errorMessage,
                    null);
        }

        Offer offer = null;
        double priceNumber = Double.parseDouble(price);

        String highestOfferQuery = "SELECT MAX(price) AS highest_offer FROM offers WHERE item_id = ?;";
        try {
            PreparedStatement highestOfferStatement = database.prepareStatement(highestOfferQuery);

            highestOfferStatement.setString(1, itemId);

            ResultSet resultSet = highestOfferStatement.executeQuery();

            if (resultSet.next()) {
                double highestOffer = resultSet.getDouble("highest_offer");

                if (priceNumber < highestOffer) {
                    return new Response<>(
                            false,
                            "Failed to create offer:\r\n- Offer must be higher than the current highest offer that is " + highestOffer + ".",
                            null
                    );
                }
            }
            else {
                String itemPriceQuery = "SELECT price FROM items where ID = ?;";

                PreparedStatement itemPriceStatement = database.prepareStatement(itemPriceQuery);

                itemPriceStatement.setString(1, itemId);

                ResultSet itemPriceResultSet = itemPriceStatement.executeQuery();

                if (itemPriceResultSet.next()) {
                    double itemPrice = itemPriceResultSet.getDouble("price");

                    if (priceNumber > itemPrice) {
                        return new Response<>(
                                false,
                                "Failed to create offer:\r\n- Offer must be lower than the item price that is " + itemPrice + ".",
                                null
                        );
                    }
                }
            }

            Response<String> response = checkLatestOfferId();
            if (!response.getIsSuccess()) {
                return new Response<>(
                        false,
                        "Failed to create offer:\r\n- " + response.getMessage(),
                        null
                );
            }

            int latestId = Integer.parseInt(response.getOutput().substring(3));
            String id = String.format("OID%04d", latestId + 1);

            offer = new Offer(
                    id,
                    userId,
                    itemId,
                    priceNumber,
                    Calendar.getInstance().getTime(),
                    OfferStatus.PENDING,
                    ""
            );

            String offerQuery = "INSERT INTO offers (id, user_id, item_id, price, date, status, reason) VALUES (?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement offerStatement = database.prepareStatement(offerQuery);

            offerStatement.setString(1, offer.getId());
            offerStatement.setString(2, offer.getUserId());
            offerStatement.setString(3, offer.getItemId());
            offerStatement.setDouble(4, offer.getPrice());
            offerStatement.setDate(5, new Date(offer.getDate().getTime()));
            offerStatement.setString(6, offer.getStatus().toString());
            offerStatement.setString(7, offer.getReason());

            offerStatement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to create offer:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Offer created successfully.",
                offer
        );
    }

    public Response<ArrayList<Item>> getOfferedItems(String sellerId) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "SELECT i.*, o.price AS offered_price FROM items AS i RIGHT JOIN offers AS o ON i.id = o.item_id WHERE i.seller_id = ? AND o.status = ?;";
        try {
            PreparedStatement statement = database.prepareStatement(query);

            statement.setString(1, sellerId);
            statement.setString(2, OfferStatus.PENDING.toString());

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
                        String.format("%1$,.2f", resultSet.getDouble("offered_price"))
                );

                items.add(item);
            }
        }
        catch (Exception ex) {
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

    public Response<Offer> acceptOffer(String id, String userId, String itemId) {
        String updateOfferQuery = "UPDATE offers SET status = ? WHERE id = ? AND user_id = ? AND item_id = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(updateOfferQuery);

            statement.setString(1, OfferStatus.ACCEPTED.toString());
            statement.setString(2, id);
            statement.setString(3, userId);
            statement.setString(4, itemId);

            statement.executeUpdate();

            Response<Transaction> response = new TransactionController().purchaseItem(userId, itemId);

            if (!response.getIsSuccess()) {
                return new Response<>(
                        false,
                        "Failed to create transaction:\r\n- " + response.getMessage(),
                        null
                );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to accept offer:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Offer accepted successfully.",
                null
        );
    }

    public Response<Offer> declineOffer(String id, String userId, String itemId, String reason) {
        String errorMessage = validateReason(reason);

        if (!errorMessage.isEmpty()) {
            return new Response<>(
                    false,
                    "Failed to decline offer:\r\n" + errorMessage,
                    null
            );
        }

        String updateOfferQuery = "UPDATE offers SET status = ?, reason = ? WHERE id = ? AND user_id = ? AND item_id = ?;";

        try {
            PreparedStatement statement = database.prepareStatement(updateOfferQuery);

            statement.setString(1, OfferStatus.ACCEPTED.toString());
            statement.setString(2, reason);
            statement.setString(3, id);
            statement.setString(4, userId);
            statement.setString(5, itemId);

            statement.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "Failed to decline offer:\r\n- " + ex.getMessage(),
                    null
            );
        }

        return new Response<>(
                true,
                "Offer declined successfully.",
                null
        );
    }

    public Response<String> checkLatestOfferId() {
        String offerId = "OID0000";

        String query = "SELECT o.id FROM offers AS o ORDER BY id DESC LIMIT 1;";
        try {
            PreparedStatement statement = database.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                offerId = resultSet.getString("id");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(
                    false,
                    "An error occurred while checking the latest offer ID.",
                    offerId
            );
        }

        return new Response<>(
                true,
                "Latest offer ID checked successfully.",
                offerId
        );
    }

    // Validations

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

    private String validateReason(String reason) {
        String errorMessage = "";

        if (reason.isEmpty()) {
            errorMessage += "- Reason cannot be empty.\r\n";
        }

        return errorMessage;
    }

}
