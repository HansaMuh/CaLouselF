package controllers;

//File: controllers/LoginController.java

import models.Database;
import models.User;
import views.LoginView;
import javafx.scene.control.Alert;
import main.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* Controller untuk mengelola logika login.
*/
public class LoginController {
 private LoginView view;
 private Main mainApp; // Referensi ke kelas Main untuk navigasi

 /**
  * Konstruktor untuk LoginController.
  * @param view LoginView yang akan dikontrol.
  * @param mainApp Referensi ke kelas Main untuk navigasi.
  */
 public LoginController(LoginView view, Main mainApp) {
     this.view = view;
     this.mainApp = mainApp;
     initialize();
 }

 /**
  * Menginisialisasi event handler untuk tombol login.
  */
 private void initialize() {
     view.getLoginButton().setOnAction(e -> loginUser());
 }

 /**
  * Menangani proses login pengguna.
  */
 private void loginUser() {
     String username = view.getUsernameField().getText().trim();
     String password = view.getPasswordField().getText().trim();

     // Validasi
     if (username.isEmpty()) {
         showAlert("Validation Error", "Username cannot be empty.");
         return;
     }
     if (password.isEmpty()) {
         showAlert("Validation Error", "Password cannot be empty.");
         return;
     }

     // Kasus khusus untuk admin
     if (username.equals("admin") && password.equals("admin")) {
         showAlert("Success", "Admin logged in successfully!");
         // Redirect ke admin dashboard
         if (mainApp != null) {
             User adminUser = new User();
             adminUser.setId(0); // ID khusus untuk admin
             adminUser.setUsername("admin");
             adminUser.setRole("Admin");
             mainApp.showHomeView(adminUser);
         }
         return;
     }

     // Periksa kredensial di database
     User user = authenticateUser(username, password);
     if (user != null) {
         showAlert("Success", "Logged in successfully as " + user.getRole() + "!");
         // Redirect ke Home berdasarkan peran
         if (mainApp != null) {
             mainApp.showHomeView(user);
         }
     } else {
         showAlert("Error", "Invalid username or password.");
     }
 }

 /**
  * Mengautentikasi pengguna berdasarkan username dan password.
  * @param username Username pengguna.
  * @param password Password pengguna.
  * @return Objek User jika autentikasi berhasil, null jika gagal.
  */
 private User authenticateUser(String username, String password) {
     String query = "SELECT * FROM users WHERE username = ? AND password = ?";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(query)) {
         stmt.setString(1, username);
         stmt.setString(2, password); // Bandingkan password langsung
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
             User user = new User();
             user.setId(rs.getInt("id"));
             user.setUsername(rs.getString("username"));
             user.setRole(rs.getString("role"));
             return user;
         }
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while authenticating.");
     }
     return null;
 }

 /**
  * Menampilkan alert ke pengguna.
  * @param title Judul alert.
  * @param message Pesan alert.
  */
 private void showAlert(String title, String message) {
     Alert.AlertType alertType;
     if (title.equalsIgnoreCase("Success")) {
         alertType = Alert.AlertType.INFORMATION;
     } else if (title.equalsIgnoreCase("Error") || title.contains("Validation")) {
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


