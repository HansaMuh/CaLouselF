package views.seller;

import controllers.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Item;
import modules.Response;
import view_controllers.MainViewController;

public class ItemEditorView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public ItemEditorView(Item currentItem) {
        this.itemController = new ItemController();
        this.currentItem = currentItem;

        init();
        setLayout();
        setDetails();
        // TODO: setStyling(); // Uncomment kalau sudah ada metode setStyling
    }

    // Properties

    private ItemController itemController;
    private Item currentItem;

    private Label titleLabel;
    private Label captionLabel;

    private GridPane formGrid;
    private GridPane buttonGrid;

    private Label nameLabel;
    private TextField nameField;

    private Label categoryLabel;
    private TextField categoryField;

    private Label sizeLabel;
    private TextField sizeField;

    private Label priceLabel;
    private TextField priceField;

    private Button saveButton;
    private Button cancelButton;

    // Methods

    private void init() {
        titleLabel = new Label("Seller's Item Editor");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel = new Label("Edit the details of the item below.");
        captionLabel.setStyle("-fx-font-size: 14px;");

        formGrid = new GridPane();
        buttonGrid = new GridPane();

        nameLabel = new Label("Name");
        nameField = new TextField();

        categoryLabel = new Label("Category");
        categoryField = new TextField();

        sizeLabel = new Label("Size");
        sizeField = new TextField();

        priceLabel = new Label("Price (IDR)");
        priceField = new TextField();

        saveButton = new Button("Save changes");
        saveButton.setOnAction(this);

        cancelButton = new Button("Discard changes");
        cancelButton.setOnAction(this);
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(5, 0, 5, 0));

        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);

        formGrid.add(categoryLabel, 0, 1);
        formGrid.add(categoryField, 1, 1);

        formGrid.add(sizeLabel, 0, 2);
        formGrid.add(sizeField, 1, 2);

        formGrid.add(priceLabel, 0, 3);
        formGrid.add(priceField, 1, 3);

        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);

        buttonGrid.add(saveButton, 0, 0);
        buttonGrid.add(cancelButton, 1, 0);

        nameField.setPrefWidth(225);
        nameField.setPromptText("Enter the item's name");

        categoryField.setPrefWidth(225);
        categoryField.setPromptText("Enter the item's category");

        sizeField.setPrefWidth(225);
        sizeField.setPromptText("Enter the item's size");

        priceField.setPrefWidth(225);
        priceField.setPromptText("Enter the item's price");

        getChildren().addAll(titleLabel, captionLabel, formGrid, buttonGrid);
    }

    private void setDetails() {
        nameField.setText(currentItem.getName());
        categoryField.setText(currentItem.getCategory());
        sizeField.setText(currentItem.getSize());
        priceField.setText(String.valueOf(currentItem.getPrice()));
    }

    private void setStyling() {
        // TODO: Implement styling
    }

    // Helpers

    private void saveChanges() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String size = sizeField.getText();
        String price = priceField.getText();

        Response<Integer> response = itemController.updateItem(currentItem.getId(), name, size, price, category);

        MainViewController.getInstance(null).showAlert(
                response.getIsSuccess(),
                response.getIsSuccess() ? "Success" : "Error",
                response.getMessage()
        );

        if (response.getIsSuccess()) {
            MainViewController.getInstance(null).navigateBack();
        }
    }

    private void cancelChanges() {
        setDetails();

        MainViewController.getInstance(null).showAlert(
                true,
                "Success",
                "Changes have been discarded."
        );
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == saveButton) {
            saveChanges();
        }
        else if (evt.getSource() == cancelButton) {
            cancelChanges();
        }
    }

}

