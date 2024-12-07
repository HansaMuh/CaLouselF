package views;

//File: views/AdminView.java

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Item;

public class AdminView {
 private VBox view;
 private TableView<Item> tableView;
 private Button approveButton;
 private Button declineButton;

 public AdminView() {
     view = new VBox();
     view.setPadding(new Insets(20));
     view.setSpacing(10);

     tableView = new TableView<>();

     TableColumn<Item, String> nameCol = new TableColumn<>("Item Name");
     nameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));

     TableColumn<Item, String> categoryCol = new TableColumn<>("Category");
     categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

     TableColumn<Item, String> sizeCol = new TableColumn<>("Size");
     sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

     TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
     priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

     TableColumn<Item, String> statusCol = new TableColumn<>("Status");
     statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

     tableView.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, statusCol);

     approveButton = new Button("Approve Selected Item");
     declineButton = new Button("Decline Selected Item");

     view.getChildren().addAll(tableView, approveButton, declineButton);
 }

 public VBox getView() {
     return view;
 }

 public TableView<Item> getTableView() { return tableView; }
 public Button getApproveButton() { return approveButton; }
 public Button getDeclineButton() { return declineButton; }
}

