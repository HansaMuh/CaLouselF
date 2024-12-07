package controllers;

//File: controllers/ViewItemsController.java

import singleton.Database;
import models.Item;
import views.ViewItemsView;
import views.EditItemView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewItemsController {
 private ViewItemsView view;
 private int sellerId;

 public ViewItemsController(ViewItemsView view, int sellerId) {
     this.view = view;
     this.sellerId = sellerId;
     initialize();
 }

 private void initialize() {
     loadItems();

     view.getEditButton().setOnAction(e -> editSelectedItem());
     view.getDeleteButton().setOnAction(e -> deleteSelectedItem());
 }

 private void loadItems() {
     ObservableList<Item> items = FXCollections.observableArrayList();
     String query = "SELECT * FROM items WHERE seller_id = ?";

     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(query)) {
         stmt.setInt(1, sellerId);
         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
             Item item = new Item();
             item.setId(rs.getInt("id"));
             item.setName(rs.getString("item_name"));
             item.setCategory(rs.getString("category"));
             item.setSize(rs.getString("size"));
             item.setPrice(rs.getDouble("price"));
             item.setStatus(rs.getString("status"));
             items.add(item);
         }
         view.getTableView().setItems(items);
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while loading items.");
     }
 }

 private void editSelectedItem() {
     TableView<Item> table = view.getTableView();
     Item selectedItem = table.getSelectionModel().getSelectedItem();
     if (selectedItem == null) {
         showAlert("Selection Error", "Please select an item to edit.");
         return;
     }

     // Buka jendela baru untuk edit item
     EditItemView editView = new EditItemView(selectedItem);
     EditItemController editController = new EditItemController(editView, selectedItem);

     // Buat scene dan stage baru
     BorderPane layout = new BorderPane();
     layout.setCenter(editView.getView());

     Scene scene = new Scene(layout, 400, 400);
     Stage editStage = new Stage();
     editStage.setTitle("Edit Item");
     editStage.setScene(scene);
     editStage.showAndWait();

     // Setelah edit selesai, reload daftar item
     loadItems();
 }

 private void deleteSelectedItem() {
     TableView<Item> table = view.getTableView();
     Item selectedItem = table.getSelectionModel().getSelectedItem();
     if (selectedItem == null) {
         showAlert("Selection Error", "Please select an item to delete.");
         return;
     }

     // Konfirmasi penghapusan
     Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
     confirm.setTitle("Confirm Delete");
     confirm.setHeaderText(null);
     confirm.setContentText("Are you sure you want to delete the selected item?");
     confirm.showAndWait().ifPresent(response -> {
         if (response == javafx.scene.control.ButtonType.OK) {
             if (deleteItem(selectedItem.getId())) {
                 showAlert("Success", "Item deleted successfully.");
                 loadItems();
             } else {
                 showAlert("Error", "Failed to delete item.");
             }
         }
     });
 }

 private boolean deleteItem(int itemId) {
     String delete = "DELETE FROM items WHERE id = ?";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(delete)) {
         stmt.setInt(1, itemId);
         int rows = stmt.executeUpdate();
         return rows > 0;
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while deleting the item.");
         return false;
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


