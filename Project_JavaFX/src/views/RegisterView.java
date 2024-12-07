package views;


//File: views/RegisterView.java

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class RegisterView {
  private VBox view;
  private TextField usernameField;
  private PasswordField passwordField;
  private TextField phoneField;
  private TextField addressField;
  private RadioButton sellerRadio;
  private RadioButton buyerRadio;
  private RadioButton adminRadio;  // Add radio button for Admin
  private Button registerButton;

  public RegisterView() {
      view = new VBox();
      view.setPadding(new Insets(20));
      view.setSpacing(10);

      Label title = new Label("Register");
      title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

      GridPane grid = new GridPane();
      grid.setVgap(10);
      grid.setHgap(10);

      Label usernameLabel = new Label("Username:");
      usernameField = new TextField();

      Label passwordLabel = new Label("Password:");
      passwordField = new PasswordField();

      Label phoneLabel = new Label("Phone Number:");
      phoneField = new TextField();

      Label addressLabel = new Label("Address:");
      addressField = new TextField();

      Label roleLabel = new Label("Role:");
      ToggleGroup roleGroup = new ToggleGroup();
      
      sellerRadio = new RadioButton("Seller");
      sellerRadio.setToggleGroup(roleGroup);
      
      buyerRadio = new RadioButton("Buyer");
      buyerRadio.setToggleGroup(roleGroup);
      
      adminRadio = new RadioButton("Admin");  // Initialize the Admin radio button
      adminRadio.setToggleGroup(roleGroup);   // Set the same toggle group

      sellerRadio.setSelected(true);  // Default role is "Seller"

      grid.add(usernameLabel, 0, 0);
      grid.add(usernameField, 1, 0);
      grid.add(passwordLabel, 0, 1);
      grid.add(passwordField, 1, 1);
      grid.add(phoneLabel, 0, 2);
      grid.add(phoneField, 1, 2);
      grid.add(addressLabel, 0, 3);
      grid.add(addressField, 1, 3);
      grid.add(roleLabel, 0, 4);
      grid.add(sellerRadio, 1, 4);
      grid.add(buyerRadio, 1, 5);
      grid.add(adminRadio, 1, 6);   // Add the Admin radio button to the grid

      registerButton = new Button("Register");

      view.getChildren().addAll(title, grid, registerButton);
  }

  public VBox getView() {
      return view;
  }

  public TextField getUsernameField() { return usernameField; }
  public PasswordField getPasswordField() { return passwordField; }
  public TextField getPhoneField() { return phoneField; }
  public TextField getAddressField() { return addressField; }
  public RadioButton getSellerRadio() { return sellerRadio; }
  public RadioButton getBuyerRadio() { return buyerRadio; }
  public RadioButton getAdminRadio() { return adminRadio; }  // Add getter for Admin radio button
  public Button getRegisterButton() { return registerButton; }
}

