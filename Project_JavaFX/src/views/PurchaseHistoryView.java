package views;

//File: views/PurchaseHistoryView.java

import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Transaction;
import models.Item;

public class PurchaseHistoryView {
 private VBox view;
 private TableView<Transaction> tableView;

 public PurchaseHistoryView() {
     view = new VBox();
     view.setPadding(new Insets(20));
     view.setSpacing(10);

     tableView = new TableView<>();

     TableColumn<Transaction, Integer> transactionIdCol = new TableColumn<>("Transaction ID");
     transactionIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

     TableColumn<Transaction, String> itemNameCol = new TableColumn<>("Item Name");
     itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName")); // Perlu diubah di controller

     TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
     categoryCol.setCellValueFactory(new PropertyValueFactory<>("category")); // Perlu diubah di controller

     TableColumn<Transaction, String> sizeCol = new TableColumn<>("Size");
     sizeCol.setCellValueFactory(new PropertyValueFactory<>("size")); // Perlu diubah di controller

     TableColumn<Transaction, Double> priceCol = new TableColumn<>("Price");
     priceCol.setCellValueFactory(new PropertyValueFactory<>("price")); // Perlu diubah di controller

     TableColumn<Transaction, String> dateCol = new TableColumn<>("Transaction Date");
     dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

     tableView.getColumns().addAll(transactionIdCol, itemNameCol, categoryCol, sizeCol, priceCol, dateCol);

     view.getChildren().addAll(tableView);
 }

 public VBox getView() {
     return view;
 }

 public TableView<Transaction> getTableView() { return tableView; }
}

