package view_controllers;

//File: view_controllers/RegisterViewController.java


import controllers.RegisterController;
import views.RegisterView;
import javafx.scene.layout.VBox;

public class RegisterViewController {
 private VBox view;
 private RegisterController controller;

 public RegisterViewController() {
     RegisterView registerView = new RegisterView();
     controller = new RegisterController(registerView);
     view = registerView.getView();
 }

 public VBox getView() {
     return view;
 }
}
