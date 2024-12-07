package view_controllers;

//File: view_controllers/AdminViewController.java

import controllers.AdminController;
import views.AdminView;
import javafx.scene.layout.VBox;

public class AdminViewController {
 private VBox view;
 private AdminController controller;

 public AdminViewController() {
     AdminView adminView = new AdminView();
     controller = new AdminController(adminView);
     view = adminView.getView();
 }

 public VBox getView() {
     return view;
 }
}

