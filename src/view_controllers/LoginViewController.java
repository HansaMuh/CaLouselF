package view_controllers;

//File: view_controllers/LoginViewController.java

import controllers.LoginController;
import views.LoginView;
import javafx.scene.layout.VBox;
import main.Main;

public class LoginViewController {
 private VBox view;
 private LoginController controller;

 public LoginViewController(Main mainApp) {
     LoginView loginView = new LoginView();
     controller = new LoginController(loginView, mainApp); // Modifikasi konstruktor LoginController
     view = loginView.getView();
 }

 public VBox getView() {
     return view;
 }
}


