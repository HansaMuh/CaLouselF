package controllers;

//File: controllers/WishlistController.java

import singleton.Database;
import models.Item;
import views.WishlistView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WishlistController {
 private WishlistView view;
 private int userId; // ID pengguna yang sedang login sebagai buyer

 public WishlistController(WishlistView view, int userId) {
     this.view = view;
     this.userId = userId;
     initialize();
 }

 private void initialize() {
     loadWishlistItems();

     view.getRemoveButton().setOnAction(e -> removeSelectedItem());
 }

 private void loadWishlistItems() {
     ObservableList<Item> items = FXCollections.observableArrayList();
     String query = "SELECT items.* FROM items INNER JOIN wishlists ON items.id = wishlists.item_id WHERE wishlists.user_id = ?";

     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(query)) {
         stmt.setInt(1, userId);
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
         showAlert("Database Error", "An error occurred while loading wishlist items.");
     }
 }

 private void removeSelectedItem() {
     TableView<Item> table = view.getTableView();
     Item selectedItem = table.getSelectionModel().getSelectedItem();
     if (selectedItem == null) {
         showAlert("Selection Error", "Please select an item to remove from wishlist.");
         return;
     }

     String delete = "DELETE FROM wishlists WHERE user_id = ? AND item_id = ?";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(delete)) {
         stmt.setInt(1, userId);
//         stmt.setInt(2, selectedItem.getId());
         int rows = stmt.executeUpdate();
         if (rows > 0) {
             showAlert("Success", "Item removed from wishlist.");
             loadWishlistItems();
         } else {
             showAlert("Error", "Failed to remove item from wishlist.");
         }
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while removing the item from wishlist.");
     }
 }

 private void showAlert(String title, String message) {
     Alert.AlertType alertType;
     if (title.equalsIgnoreCase("Success") || title.equalsIgnoreCase("Info")) {
         alertType = Alert.AlertType.INFORMATION;
     } else if (title.contains("Error") || title.contains("Selection")) {
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

