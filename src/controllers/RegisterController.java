package controllers;

//File: controllers/RegisterController.java

import models.Database;
import models.User;
import views.RegisterView;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* Controller untuk mengelola logika registrasi pengguna.
*/
public class RegisterController {
 private RegisterView view;

 /**
  * Konstruktor untuk RegisterController.
  * @param view Tampilan registrasi yang akan dikontrol.
  */
 public RegisterController(RegisterView view) {
     this.view = view;
     initialize();
 }

 /**
  * Menginisialisasi event handler untuk tombol registrasi.
  */
 private void initialize() {
     view.getRegisterButton().setOnAction(e -> registerUser());
 }

 /**
  * Metode untuk menangani proses registrasi pengguna.
  */
 private void registerUser() {
     String username = view.getUsernameField().getText().trim();
     String password = view.getPasswordField().getText().trim();
     String phone = view.getPhoneField().getText().trim();
     String address = view.getAddressField().getText().trim();
     RadioButton selectedRole = (RadioButton) view.getSellerRadio().getToggleGroup().getSelectedToggle();
     String role = selectedRole.getText();

     // Validasi
     if (username.isEmpty()) {
         showAlert("Validation Error", "Username cannot be empty.");
         return;
     }
     if (username.length() < 3) {
         showAlert("Validation Error", "Username must be at least 3 characters long.");
         return;
     }
     if (!isUsernameUnique(username)) {
         showAlert("Validation Error", "Username already exists.");
         return;
     }
     if (password.isEmpty()) {
         showAlert("Validation Error", "Password cannot be empty.");
         return;
     }
     if (password.length() < 8) {
         showAlert("Validation Error", "Password must be at least 8 characters long.");
         return;
     }
     if (!containsSpecialCharacter(password)) {
         showAlert("Validation Error", "Password must include at least one special character (!, @, #, $, %, ^, &, *).");
         return;
     }
     if (!isValidPhoneNumber(phone)) {
         showAlert("Validation Error", "Phone number must start with +62 and be at least 10 characters long.");
         return;
     }
     if (address.isEmpty()) {
         showAlert("Validation Error", "Address cannot be empty.");
         return;
     }
     if (role == null || role.isEmpty()) {
         showAlert("Validation Error", "Please select a role.");
         return;
     }

     // Buat user baru tanpa hashing password
     User user = new User(username, password, phone, address, role);
     if (saveUser(user)) {
         showAlert("Success", "Registration successful!");
         clearFields();
     } else {
         showAlert("Error", "Registration failed. Please try again.");
     }
 }

 /**
  * Memeriksa apakah username unik.
  * @param username Username yang akan diperiksa.
  * @return true jika unik, false jika sudah ada.
  */
 private boolean isUsernameUnique(String username) {
     String query = "SELECT * FROM users WHERE username = ?";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(query)) {
         stmt.setString(1, username);
         ResultSet rs = stmt.executeQuery();
         return !rs.next();
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while checking username uniqueness.");
         return false;
     }
 }

 /**
  * Memeriksa apakah password mengandung karakter khusus.
  * @param password Password yang akan diperiksa.
  * @return true jika mengandung, false jika tidak.
  */
 private boolean containsSpecialCharacter(String password) {
     String specialChars = "!@#$%^&*";
     for (char c : password.toCharArray()) {
         if (specialChars.indexOf(c) != -1) return true;
     }
     return false;
 }

 /**
  * Memeriksa validitas nomor telepon.
  * @param phone Nomor telepon yang akan diperiksa.
  * @return true jika valid, false jika tidak.
  */
 private boolean isValidPhoneNumber(String phone) {
     if (!phone.startsWith("+62")) return false;
     if (phone.length() < 10) return false; // termasuk +62
     String numberPart = phone.substring(3);
     try {
         Long.parseLong(numberPart);
         return numberPart.length() >= 8; // total panjang termasuk +62 >=10
     } catch (NumberFormatException e) {
         return false;
     }
 }

 /**
  * Menyimpan user ke database.
  * @param user Objek User yang akan disimpan.
  * @return true jika berhasil, false jika gagal.
  */
 private boolean saveUser(User user) {
     String insert = "INSERT INTO users (username, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?)";
     try (Connection conn = Database.getConnection();
          PreparedStatement stmt = conn.prepareStatement(insert)) {
         stmt.setString(1, user.getUsername());
         stmt.setString(2, user.getPassword()); // Simpan password langsung
         stmt.setString(3, user.getPhoneNumber());
         stmt.setString(4, user.getAddress());
         stmt.setString(5, user.getRole());
         int rows = stmt.executeUpdate();
         return rows > 0;
     } catch (SQLException e) {
         showAlert("Database Error", "An error occurred while saving the user.");
         return false;
     }
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

 /**
  * Mengosongkan field input setelah registrasi berhasil.
  */
 private void clearFields() {
     view.getUsernameField().clear();
     view.getPasswordField().clear();
     view.getPhoneField().clear();
     view.getAddressField().clear();
     view.getSellerRadio().setSelected(true);
 }
}



