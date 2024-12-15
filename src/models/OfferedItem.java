package models;

import enums.ItemStatus;

/*
    OfferedItem class represents an item that is being offered for purchase.
    This class is primarily used to display items offered by buyers to a seller on the platform.
    No database interaction is done with this class.
 */
public class OfferedItem extends Item {

    // Constructor

    public OfferedItem(String offerId, double offeredPrice, String id, String sellerId, String name, String size, double price,
                       String category, ItemStatus status, String note) {
        super(id, sellerId, name, size, price, category, status, note);

        this.offerId = offerId;
        this.offeredPrice = offeredPrice;
    }

    // Properties

    private String offerId;
    private double offeredPrice;

    // Getters

    public String getOfferId() {
        return offerId;
    }

    public double getOfferedPrice() {
        return offeredPrice;
    }

}
