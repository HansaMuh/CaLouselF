package models;

import enums.ItemStatus;

/*
    TransactionalItem class represents a purchased item in the system that is associated with a transaction.
    This class is primarily used to display items bought by a buyer on the platform.
    No database interaction is done with this class.
 */
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
