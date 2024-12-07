package view_controllers;

//File: view_controllers/ViewItemsViewController.java

import controllers.ViewItemsController;
import views.ViewItemsView;
import javafx.scene.layout.VBox;

public class ViewItemsViewController {
 private VBox view;
 private ViewItemsController controller;

 public ViewItemsViewController(int sellerId) {
     ViewItemsView viewItemsView = new ViewItemsView();
     controller = new ViewItemsController(viewItemsView, sellerId);
     view = viewItemsView.getView();
 }

 public VBox getView() {
     return view;
 }
}


