package models;

import enums.ItemStatus;

public class TransactionalItem extends Item {

    // Constructor

    public TransactionalItem(String transactionId, String id, String sellerId, String name, String size, double price,
                       String category, ItemStatus status, String note) {
        super(id, sellerId, name, size, price, category, status, note);

        this.transactionId = transactionId;
    }

    // Properties

    private String transactionId;

    // Getters

    public String getTransactionId() {
        return transactionId;
    }

}
