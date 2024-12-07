package views;

//File: views/BuyerView.java

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Item;

public class BuyerView {
 private VBox view;
 private TableView<Item> tableView;
 private Button purchaseButton;
 private Button makeOfferButton;
 private Button addToWishlistButton;

 /**
  * Konstruktor untuk BuyerView.
  * Menginisialisasi tampilan tabel dan tombol-tombol aksi.
  */
 public BuyerView() {
     view = new VBox();
     view.setPadding(new Insets(20));
     view.setSpacing(10);

     tableView = new TableView<>();

     // Definisikan kolom-kolom tabel
     TableColumn<Item, String> nameCol = new TableColumn<>("Item Name");
     nameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));

     TableColumn<Item, String> categoryCol = new TableColumn<>("Category");
     categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

     TableColumn<Item, String> sizeCol = new TableColumn<>("Size");
     sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

     TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
     priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

     tableView.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol);

     // Inisialisasi tombol aksi
     purchaseButton = new Button("Purchase Selected Item");
     makeOfferButton = new Button("Make Offer on Selected Item");
     addToWishlistButton = new Button("Add Selected Item to Wishlist");

     view.getChildren().addAll(tableView, purchaseButton, makeOfferButton, addToWishlistButton);
 }

 /**
  * Mendapatkan tampilan VBox.
  * @return VBox yang berisi TableView dan tombol-tombol aksi.
  */
 public VBox getView() {
     return view;
 }

 // Getters untuk komponen-komponen yang diperlukan oleh controller

 public TableView<Item> getTableView() { return tableView; }
 public Button getPurchaseButton() { return purchaseButton; }
 public Button getMakeOfferButton() { return makeOfferButton; }
 public Button getAddToWishlistButton() { return addToWishlistButton; }
}

