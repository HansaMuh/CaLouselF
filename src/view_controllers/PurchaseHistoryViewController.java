package view_controllers;

//File: view_controllers/PurchaseHistoryViewController.java

import controllers.PurchaseHistoryController;
import views.PurchaseHistoryView;
import javafx.scene.layout.VBox;

public class PurchaseHistoryViewController {
 private VBox view;
 private PurchaseHistoryController controller;

 public PurchaseHistoryViewController(int userId) {
//     PurchaseHistoryView purchaseHistoryView = new PurchaseHistoryView();
//     controller = new PurchaseHistoryController(purchaseHistoryView, userId);
//     view = purchaseHistoryView.getView();
 }

 public VBox getView() {
     return view;
 }
}

