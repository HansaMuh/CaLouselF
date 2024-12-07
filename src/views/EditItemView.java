package views;

//File: views/EditItemView.java

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Item; // Pastikan mengimpor kelas Item

public class EditItemView {
 private VBox view;
 private TextField itemNameField;
 private TextField categoryField;
 private TextField sizeField;
 private TextField priceField;
 private Button saveButton;
 private Button cancelButton;

 public EditItemView(Item item) {
     view = new VBox();
     view.setPadding(new Insets(20));
     view.setSpacing(10);

     Label title = new Label("Edit Item");
     title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

     GridPane grid = new GridPane();
     grid.setVgap(10);
     grid.setHgap(10);

     Label itemNameLabel = new Label("Item Name:");
     itemNameField = new TextField(item.getName());

     Label categoryLabel = new Label("Category:");
     categoryField = new TextField(item.getCategory());

     Label sizeLabel = new Label("Size:");
     sizeField = new TextField(item.getSize());

     Label priceLabel = new Label("Price:");
     priceField = new TextField(String.valueOf(item.getPrice()));

     grid.add(itemNameLabel, 0, 0);
     grid.add(itemNameField, 1, 0);
     grid.add(categoryLabel, 0, 1);
     grid.add(categoryField, 1, 1);
     grid.add(sizeLabel, 0, 2);
     grid.add(sizeField, 1, 2);
     grid.add(priceLabel, 0, 3);
     grid.add(priceField, 1, 3);

     saveButton = new Button("Save Changes");
     cancelButton = new Button("Cancel");

     view.getChildren().addAll(title, grid, saveButton, cancelButton);
 }

 public VBox getView() {
     return view;
 }

 public TextField getItemNameField() { return itemNameField; }
 public TextField getCategoryField() { return categoryField; }
 public TextField getSizeField() { return sizeField; }
 public TextField getPriceField() { return priceField; }
 public Button getSaveButton() { return saveButton; }
 public Button getCancelButton() { return cancelButton; }
}

