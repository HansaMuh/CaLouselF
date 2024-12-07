package controllers;

//File: controllers/AdminController.java

import singleton.Database;
import models.Item;
import views.AdminView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminController {
 private AdminView view;

 public AdminController(AdminView view) {
     this.view = view;
     initialize();
 }

 private void initialize() {
     loadPendingItems();

     view.getApproveButton().setOnAction(e -> approveSelectedItem());
     view.getDeclineButton().setOnAction(e -> declineSelectedItem());
 }

 private void loadPendingItems() {
     ObservableList<Item> items = FXCollections.observableArrayList();
     String query = "SELECT * FROM items WHERE status = 'Pending'";

     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(query)) {
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
//             Item item = new Item();
//             item.setId(rs.getInt("id"));
//             item.setName(rs.getString("item_name"));
//             item.setCategory(rs.getString("category"));
//             item.setSize(rs.getString("size"));
//             item.setPrice(rs.getDouble("price"));
//             item.setStatus(rs.getString("status"));
//             items.add(item);
         }
         view.getTableView().setItems(items);
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while loading pending items.");
     }
 }

 private void approveSelectedItem() {
     TableView<Item> table = view.getTableView();
     Item selectedItem = table.getSelectionModel().getSelectedItem();
     if (selectedItem == null) {
         showAlert("Selection Error", "Please select an item to approve.");
         return;
     }

     String update = "UPDATE items SET status = 'Approved', reason = NULL WHERE id = ?";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(update)) {
//         stmt.setInt(1, selectedItem.getId());
         int rows = stmt.executeUpdate();
         if (rows > 0) {
             showAlert("Success", "Item approved successfully.");
             loadPendingItems();
         } else {
             showAlert("Error", "Failed to approve item.");
         }
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while approving the item.");
     }
 }

 private void declineSelectedItem() {
     TableView<Item> table = view.getTableView();
     Item selectedItem = table.getSelectionModel().getSelectedItem();
     if (selectedItem == null) {
         showAlert("Selection Error", "Please select an item to decline.");
         return;
     }

     // Minta alasan penolakan
     TextInputDialog dialog = new TextInputDialog();
     dialog.setTitle("Decline Item");
     dialog.setHeaderText("Provide a reason for declining the item:");
     dialog.setContentText("Reason:");

     Optional<String> result = dialog.showAndWait();
     if (result.isPresent()) {
         String reason = result.get().trim();
         if (reason.isEmpty()) {
             showAlert("Validation Error", "Reason cannot be empty.");
             return;
         }

         String update = "UPDATE items SET status = 'Declined', reason = ? WHERE id = ?";
         try (Connection conn = Database.getConnection();
              PreparedStatement stmt = conn.prepareStatement(update)) {
             stmt.setString(1, reason);
//             stmt.setInt(2, selectedItem.getId());
             int rows = stmt.executeUpdate();
             if (rows > 0) {
                 showAlert("Success", "Item declined successfully.");
                 loadPendingItems();
             } else {
                 showAlert("Error", "Failed to decline item.");
             }
         } catch (SQLException e) {
             showAlert("Database Error", "An error occurred while declining the item.");
         }
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
