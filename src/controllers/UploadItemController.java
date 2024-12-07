package controllers;

//File: controllers/UploadItemController.java


import singleton.Database;
import models.Item;
import views.UploadItemView;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UploadItemController {
 private UploadItemView view;
 private int sellerId; // ID pengguna yang sedang login sebagai seller

 public UploadItemController(UploadItemView view, int sellerId) {
     this.view = view;
     this.sellerId = sellerId;
     initialize();
 }

 private void initialize() {
     view.getUploadButton().setOnAction(e -> uploadItem());
 }

 private void uploadItem() {
     String itemName = view.getItemNameField().getText().trim();
     String category = view.getCategoryField().getText().trim();
     String size = view.getSizeField().getText().trim();
     String priceStr = view.getPriceField().getText().trim();

     // Validasi
     if (itemName.isEmpty()) {
         showAlert("Validation Error", "Item Name cannot be empty.");
         return;
     }
     if (itemName.length() < 3) {
         showAlert("Validation Error", "Item Name must be at least 3 characters long.");
         return;
     }
     if (category.isEmpty()) {
         showAlert("Validation Error", "Category cannot be empty.");
         return;
     }
     if (category.length() < 3) {
         showAlert("Validation Error", "Category must be at least 3 characters long.");
         return;
     }
     if (size.isEmpty()) {
         showAlert("Validation Error", "Size cannot be empty.");
         return;
     }
     if (priceStr.isEmpty()) {
         showAlert("Validation Error", "Price cannot be empty.");
         return;
     }

     double price;
     try {
         price = Double.parseDouble(priceStr);
         if (price <= 0) {
             showAlert("Validation Error", "Price must be greater than 0.");
             return;
         }
     } catch (NumberFormatException e) {
         showAlert("Validation Error", "Price must be a valid number.");
         return;
     }

     // Membuat objek Item
//     Item item = new Item(itemName, category, size, price, sellerId);
//
//     // Menyimpan item ke database
//     if (saveItem(item)) {
//         showAlert("Success", "Item uploaded successfully and is pending approval.");
//         clearFields();
//     } else {
//         showAlert("Error", "Failed to upload item. Please try again.");
//     }
 }

 private boolean saveItem(Item item) {
     String insert = "INSERT INTO items (item_name, category, size, price, seller_id) VALUES (?, ?, ?, ?, ?)";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(insert)) {
         stmt.setString(1, item.getName());
         stmt.setString(2, item.getCategory());
         stmt.setString(3, item.getSize());
         stmt.setDouble(4, item.getPrice());
         //stmt.setInt(5, item.getSellerId());
         int rows = stmt.executeUpdate();
         return rows > 0;
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while uploading the item.");
         return false;
     }
 }

 private void showAlert(String title, String message) {
     Alert.AlertType alertType;
     if (title.equalsIgnoreCase("Success")) {
         alertType = Alert.AlertType.INFORMATION;
     } else if (title.contains("Error") || title.contains("Validation")) {
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

 private void clearFields() {
     view.getItemNameField().clear();
     view.getCategoryField().clear();
     view.getSizeField().clear();
     view.getPriceField().clear();
 }
}

