package controllers;

//File: controllers/PurchaseHistoryController.java

import singleton.Database;
import models.Transaction;
import views.PurchaseHistoryView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseHistoryController {
 private PurchaseHistoryView view;
 private int userId; // ID pengguna yang sedang login sebagai buyer

 public PurchaseHistoryController(PurchaseHistoryView view, int userId) {
     this.view = view;
     this.userId = userId;
     initialize();
 }

 private void initialize() {
     loadPurchaseHistory();
 }

 private void loadPurchaseHistory() {
     ObservableList<Transaction> transactions = FXCollections.observableArrayList();
     String query = "SELECT transactions.id, transactions.transaction_date, items.item_name, items.category, items.size, items.price " +
                    "FROM transactions INNER JOIN items ON transactions.item_id = items.id " +
                    "WHERE transactions.user_id = ?";

     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(query)) {
         stmt.setInt(1, userId);
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
//             Transaction transaction = new Transaction();
//             transaction.setId(rs.getInt("id"));
//             transaction.setTransactionDate(rs.getString("transaction_date"));
//             transaction.setItemName(rs.getString("item_name"));
//             transaction.setCategory(rs.getString("category"));
//             transaction.setSize(rs.getString("size"));
//             transaction.setPrice(rs.getDouble("price"));
//             transactions.add(transaction);
         }
         //view.getTableView().setItems(transactions);
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while loading purchase history.");
     }
 }

 private void showAlert(String title, String message) {
     Alert.AlertType alertType;
     if (title.equalsIgnoreCase("Success") || title.equalsIgnoreCase("Info")) {
         alertType = Alert.AlertType.INFORMATION;
     } else if (title.contains("Error") || title.contains("Validation") || title.contains("Selection")) {
         alertType = Alert.AlertType.ERROR;
     } else {
         alertType = Alert.AlertType.INFORMATION;
     }

     Alert alert = new Alert(alertType);
     alert.setTitle(title);
     alert.setHeaderText(null);
     alert.setContentText(message);
     alert.showAndWait();
 }
}

