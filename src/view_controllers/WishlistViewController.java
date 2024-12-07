package view_controllers;

//File: view_controllers/WishlistViewController.java

import controllers.WishlistController;
import views.WishlistView;
import javafx.scene.layout.VBox;

public class WishlistViewController {
 private VBox view;
 private WishlistController controller;

 public WishlistViewController(int userId) {
     WishlistView wishlistView = new WishlistView();
     controller = new WishlistController(wishlistView, userId);
     view = wishlistView.getView();
 }

 public VBox getView() {
     return view;
 }
}
