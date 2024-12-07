package view_controllers;

//File: view_controllers/BuyerViewController.java

import controllers.BuyerController;
import views.BuyerView;
import javafx.scene.layout.VBox;

public class BuyerViewController {
 private VBox view;
 private BuyerController controller;

 /**
  * Konstruktor untuk BuyerViewController.
  * @param userId ID pengguna yang sedang login sebagai buyer.
  */
 public BuyerViewController(int userId) {
     BuyerView buyerView = new BuyerView();
     controller = new BuyerController(buyerView, userId);
     view = buyerView.getView();
 }

 /**
  * Mendapatkan tampilan VBox dari BuyerView.
  * @return VBox yang berisi BuyerView.
  */
 public VBox getView() {
     return view;
 }
}

