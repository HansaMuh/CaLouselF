package views;

//File: views/UploadItemView.java


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class UploadItemView {
 private VBox view;
 private TextField itemNameField;
 private TextField categoryField;
 private TextField sizeField;
 private TextField priceField;
 private Button uploadButton;

 public UploadItemView() {
     view = new VBox();
     view.setPadding(new Insets(20));
     view.setSpacing(10);

     Label title = new Label("Upload New Item");
     title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

     GridPane grid = new GridPane();
     grid.setVgap(10);
     grid.setHgap(10);

     Label itemNameLabel = new Label("Item Name:");
     itemNameField = new TextField();

     Label categoryLabel = new Label("Category:");
     categoryField = new TextField();

     Label sizeLabel = new Label("Size:");
     sizeField = new TextField();

     Label priceLabel = new Label("Price:");
     priceField = new TextField();

     grid.add(itemNameLabel, 0, 0);
     grid.add(itemNameField, 1, 0);
     grid.add(categoryLabel, 0, 1);
     grid.add(categoryField, 1, 1);
     grid.add(sizeLabel, 0, 2);
     grid.add(sizeField, 1, 2);
     grid.add(priceLabel, 0, 3);
     grid.add(priceField, 1, 3);

     uploadButton = new Button("Upload Item");

     view.getChildren().addAll(title, grid, uploadButton);
 }

 public VBox getView() {
     return view;
 }

 public TextField getItemNameField() { return itemNameField; }
 public TextField getCategoryField() { return categoryField; }
 public TextField getSizeField() { return sizeField; }
 public TextField getPriceField() { return priceField; }
 public Button getUploadButton() { return uploadButton; }
}

