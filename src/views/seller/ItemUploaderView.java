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
import models.User;
import modules.Response;
import singleton.UserAuthenticator;
import view_controllers.MainViewController;

public class ItemUploaderView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public ItemUploaderView() {
        this.itemController = new ItemController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        setStyling(); 
    }

    // Properties

    private ItemController itemController;
    private User currentUser;

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

    private Button uploadButton;

    // Methods

    private void init() {
        titleLabel = new Label("Seller's Item Uploader");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel = new Label("Fill out the form below to upload a new item.");
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

        uploadButton = new Button("Upload new item");
        uploadButton.setOnAction(this);
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

        buttonGrid.add(uploadButton, 0, 0);

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

    private void setStyling() {
        // Main VBox styling
        setStyle("-fx-background-color: #f4f6f7; -fx-padding: 20px; -fx-spacing: 15px;");

        // Title Label styling
        titleLabel.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #34495e; " +  // Dark text color for the title
            "-fx-padding: 10px 0;"
        );

        // Caption Label styling
        captionLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #7f8c8d; " +  // Lighter gray for caption text
            "-fx-padding: 5px 0;"
        );

        // Form Grid styling
        formGrid.setStyle(
            "-fx-background-color: #ffffff; " +  // White background for the form
            "-fx-border-color: #bdc3c7; " +  // Light gray border
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 5px; " +
            "-fx-padding: 15px;"
        );

        // Styling for Labels in the form
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        sizeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");

        // Styling for TextFields
        nameField.setStyle(
            "-fx-background-color: #ffffff; " +  // White background
            "-fx-border-color: #bdc3c7; " +  // Light gray border
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 5px; " +
            "-fx-padding: 5px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2c3e50;"  // Dark text color
        );
        categoryField.setStyle(nameField.getStyle());
        sizeField.setStyle(nameField.getStyle());
        priceField.setStyle(nameField.getStyle());

        // Button styling
        uploadButton.setStyle(
            "-fx-background-color: #3498db; " +  // Blue color for the upload button
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        );

        // Button hover effect
        uploadButton.setOnMouseEntered(e -> 
            uploadButton.setStyle(
                "-fx-background-color: #2980b9; " +  // Darker blue on hover
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );
        uploadButton.setOnMouseExited(e -> 
            uploadButton.setStyle(
                "-fx-background-color: #3498db; " +  // Original blue color
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );
    }


    // Helpers

    private void uploadNewItem() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String size = sizeField.getText();
        String price = priceField.getText();

        Response<Integer> response = itemController.uploadItem(currentUser.getId(), name, size, price, category);

        MainViewController.getInstance(null).showAlert(
            response.getIsSuccess(),
            response.getIsSuccess() ? "Success" : "Error",
            response.getMessage()
        );

        if (response.getIsSuccess()) {
            MainViewController.getInstance(null).navigateBack();
        }
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == uploadButton) {
            uploadNewItem();
        }
    }

}
