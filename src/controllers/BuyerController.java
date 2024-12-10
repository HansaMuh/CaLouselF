package controllers;

// Unused

//File: controllers/BuyerController.java

import singleton.Database;
import models.Item;
import views.BuyerView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* Controller untuk mengelola logika dan interaksi pada BuyerView.
*/
public class BuyerController {
 private BuyerView view;
 private int userId; // ID pengguna yang sedang login sebagai buyer

 /**
  * Konstruktor untuk BuyerController.
  * @param view BuyerView yang akan dikontrol.
  * @param userId ID pengguna yang sedang login.
  */
 public BuyerController(BuyerView view, int userId) {
     this.view = view;
     this.userId = userId;
     initialize();
 }

 /**
  * Menginisialisasi event handler untuk tombol-tombol di BuyerView.
  */
 private void initialize() {
     loadApprovedItems();
     // Tambahkan event handler lainnya jika diperlukan
 }

 /**
  * Memuat semua item yang telah disetujui dari database dan menampilkannya di tabel.
  */
 private void loadApprovedItems() {
     ObservableList<Item> items = FXCollections.observableArrayList();
     String query = "SELECT * FROM items WHERE status = 'Approved'";

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
         showAlert("Database Error", "An error occurred while loading approved items.");
         e.printStackTrace(); // Untuk debugging
     }
 }

 /**
  * Menampilkan alert ke pengguna.
  * @param title Judul alert.
  * @param message Pesan alert.
  */
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


