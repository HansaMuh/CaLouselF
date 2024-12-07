package views;

//File: views/ViewItemsView.java

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import models.Item;

public class ViewItemsView {
 private VBox view;
 private TableView<Item> tableView;
 private Button editButton;
 private Button deleteButton;

 public ViewItemsView() {
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

     editButton = new Button("Edit Selected Item");
     deleteButton = new Button("Delete Selected Item");

     view.getChildren().addAll(tableView, editButton, deleteButton);
 }

 public VBox getView() {
     return view;
 }

 public TableView<Item> getTableView() { return tableView; }
 public Button getEditButton() { return editButton; }
 public Button getDeleteButton() { return deleteButton; }
}
