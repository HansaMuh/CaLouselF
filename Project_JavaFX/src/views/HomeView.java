package views;

//File: views/HomeView.java

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class HomeView {
 private BorderPane view;
 private Button uploadItemButton;
 private Button viewItemsButton;
 private Button logoutButton;
 private Button viewWishlistButton;
 private Button viewPurchaseHistoryButton;

 public HomeView(String role) {
     view = new BorderPane();
     view.setPadding(new Insets(20));

     HBox topBar = new HBox();
     topBar.setSpacing(10);

     logoutButton = new Button("Logout");
     topBar.getChildren().add(logoutButton);

     view.setTop(topBar);

     if (role.equalsIgnoreCase("Seller")) {
         uploadItemButton = new Button("Upload New Item");
         viewItemsButton = new Button("View My Items");
         BorderPane centerPane = new BorderPane();
         centerPane.setLeft(uploadItemButton);
         centerPane.setRight(viewItemsButton);
         view.setCenter(centerPane);
     } else if (role.equalsIgnoreCase("Admin")) {
         // Admin tidak memiliki tombol khusus di sini
         // Semua kontrol diatur oleh AdminViewController
     } else if (role.equalsIgnoreCase("Buyer")) {
         // Tambahkan tombol untuk buyer
         viewWishlistButton = new Button("View Wishlist");
         viewPurchaseHistoryButton = new Button("View Purchase History");
         BorderPane centerPane = new BorderPane();
         centerPane.setLeft(viewWishlistButton);
         centerPane.setRight(viewPurchaseHistoryButton);
         view.setCenter(centerPane);
     }
 }

 public BorderPane getView() {
     return view;
 }

 public Button getUploadItemButton() { return uploadItemButton; }
 public Button getViewItemsButton() { return viewItemsButton; }
 public Button getLogoutButton() { return logoutButton; }
 public Button getViewWishlistButton() { return viewWishlistButton; }
 public Button getViewPurchaseHistoryButton() { return viewPurchaseHistoryButton; }
}


