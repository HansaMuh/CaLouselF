package controllers;

//File: controllers/EditItemController.java

import models.Database;
import models.Item;
import views.EditItemView;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditItemController {
 private EditItemView view;
 private Item item;

 public EditItemController(EditItemView view, Item item) {
     this.view = view;
     this.item = item;
     initialize();
 }

 private void initialize() {
     view.getSaveButton().setOnAction(e -> saveChanges());
     view.getCancelButton().setOnAction(e -> cancelEdit());
 }

 private void saveChanges() {
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

     // Update objek Item
     item.setItemName(itemName);
     item.setCategory(category);
     item.setSize(size);
     item.setPrice(price);
     item.setStatus("Pending"); // Set status kembali ke pending setelah edit

     // Simpan perubahan ke database
     if (updateItem(item)) {
         showAlert("Success", "Item updated successfully and is pending approval.");
         // Tutup jendela edit atau refresh daftar item
         // Implementasikan sesuai kebutuhan
     } else {
         showAlert("Error", "Failed to update item. Please try again.");
     }
 }

 private boolean updateItem(Item item) {
     String update = "UPDATE items SET item_name = ?, category = ?, size = ?, price = ?, status = ? WHERE id = ?";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(update)) {
         stmt.setString(1, item.getItemName());
         stmt.setString(2, item.getCategory());
         stmt.setString(3, item.getSize());
         stmt.setDouble(4, item.getPrice());
         stmt.setString(5, item.getStatus());
         stmt.setInt(6, item.getId());
         int rows = stmt.executeUpdate();
         return rows > 0;
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while updating the item.");
         return false;
     }
 }

 private void cancelEdit() {
     // Implementasikan logika untuk membatalkan edit, misalnya menutup jendela atau kembali ke daftar item
     // Untuk saat ini, tampilkan pesan sederhana
     showAlert("Info", "Edit canceled.");
 }

 private void showAlert(String title, String message) {
     Alert.AlertType alertType;
     if (title.equalsIgnoreCase("Success") || title.equalsIgnoreCase("Info")) {
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
}
