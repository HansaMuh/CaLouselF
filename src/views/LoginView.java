package views;

//File: views/LoginView.java

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginView {
 private VBox view;
 private TextField usernameField;
 private PasswordField passwordField;
 private Button loginButton;

 public LoginView() {
     view = new VBox();
     view.setPadding(new Insets(20));
     view.setSpacing(10);

     Label title = new Label("Login");
     title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

     GridPane grid = new GridPane();
     grid.setVgap(10);
     grid.setHgap(10);

     Label usernameLabel = new Label("Username:");
     usernameField = new TextField();

     Label passwordLabel = new Label("Password:");
     passwordField = new PasswordField();

     grid.add(usernameLabel, 0, 0);
     grid.add(usernameField, 1, 0);
     grid.add(passwordLabel, 0, 1);
     grid.add(passwordField, 1, 1);

     loginButton = new Button("Login");

     view.getChildren().addAll(title, grid, loginButton);
 }

 public VBox getView() {
     return view;
 }

 public TextField getUsernameField() { return usernameField; }
 public PasswordField getPasswordField() { return passwordField; }
 public Button getLoginButton() { return loginButton; }
}

