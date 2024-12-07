package view_controllers;

//File: view_controllers/UploadItemViewController.java

import controllers.UploadItemController;
import views.UploadItemView;
import javafx.scene.layout.VBox;

public class UploadItemViewController {
 private VBox view;
 private UploadItemController controller;

 public UploadItemViewController(int sellerId) {
     UploadItemView uploadItemView = new UploadItemView();
     controller = new UploadItemController(uploadItemView, sellerId);
     view = uploadItemView.getView();
 }

 public VBox getView() {
     return view;
 }
}

