package views.subviews;

import controllers.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Item;
import modules.Response;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class AdminHomeSubview extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public AdminHomeSubview() {
        this.itemController = new ItemController();

        init();
        setLayout();
        setTable();
        setStyling(); 
    }

    // Properties

    private ItemController itemController;

    private TableView<Item> itemsTable;

    private Label tipsArea;

    private BorderPane bottomPane;
    private HBox leftSideBox;
    private HBox declineItemBox;

    private Button approveItemButton;
    private Button declineItemButton;
    private TextField reasonField;

    // Methods

    private void init() {
        itemsTable = new TableView<>();

        tipsArea = new Label("*) Listed items above are items requested by sellers. " +
                "You can select and then approve or decline an item from here.");

        bottomPane = new BorderPane();
        leftSideBox = new HBox();
        declineItemBox = new HBox();

        approveItemButton = new Button("Approve item");
        approveItemButton.setOnAction(this);

        declineItemButton = new Button("Decline item");
        declineItemButton.setOnAction(this);

        reasonField = new TextField();
        reasonField.setPromptText("Reason for declining");
    }

    private void setLayout() {
        setSpacing(10);

        reasonField.setPrefWidth(200);

        declineItemBox.getChildren().addAll(declineItemButton, reasonField);

        leftSideBox.setSpacing(10);
        leftSideBox.getChildren().addAll(approveItemButton, declineItemBox);

        bottomPane.setLeft(leftSideBox);

        getChildren().addAll(itemsTable, tipsArea, bottomPane);
    }

    private void setTable() {
        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price (IDR)");

        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("category"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("price"));

        itemsTable.getColumns().addAll(nameColumn, categoryColumn, sizeColumn, priceColumn);

        // Autofit columns
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTableContent(null);
    }

    private void setStyling() {
        // Styling untuk layout utama
        setStyle("-fx-background-color: #f4f7fc; -fx-padding: 20px; -fx-spacing: 15px;");

        // Styling tips area (label)
        tipsArea.setStyle(
            "-fx-font-size: 12px; " +
            "-fx-text-fill: #7f8c8d; " +  // Light gray color
            "-fx-padding: 10px 0 0 0;"
        );

        // Styling tombol
        String buttonStyle =
            "-fx-background-color: #28a745; " + // Green color for approve button
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;" +
            "-fx-cursor: hand;";

        String declineButtonStyle =
            "-fx-background-color: #dc3545; " + // Red color for decline button
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;" +
            "-fx-cursor: hand;";

        approveItemButton.setStyle(buttonStyle);
        declineItemButton.setStyle(declineButtonStyle);

        // Hover efek untuk tombol
        approveItemButton.setOnMouseEntered(e -> approveItemButton.setStyle(buttonStyle + "-fx-background-color: #218838;"));
        approveItemButton.setOnMouseExited(e -> approveItemButton.setStyle(buttonStyle));

        declineItemButton.setOnMouseEntered(e -> declineItemButton.setStyle(declineButtonStyle + "-fx-background-color: #c82333;"));
        declineItemButton.setOnMouseExited(e -> declineItemButton.setStyle(declineButtonStyle));

        // Styling alasan (reasonField)
        reasonField.setStyle(
            "-fx-border-color: #dcdcdc; " +
            "-fx-padding: 5px; " +
            "-fx-font-size: 14px;"
        );

        // Styling tabel
        itemsTable.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #dcdcdc; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2c3e50;" // Dark text
        );

        // Alternating row colors
        itemsTable.setRowFactory(tv -> new TableRow<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-background-color: white;");
                } else if (getIndex() % 2 == 0) {
                    setStyle("-fx-background-color: #f9f9f9;"); // Light gray for even rows
                } else {
                    setStyle("-fx-background-color: white;"); // White for odd rows
                }
            }
        });

        // Styling header kolom tabel
        for (TableColumn<Item, ?> column : itemsTable.getColumns()) {
            column.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495e;");
        }

        // Styling bottomPane
        bottomPane.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-padding: 10px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px 0 0 0; " +
            "-fx-border-radius: 5px;"
        );

        // Styling untuk HBox di leftSideBox dan declineItemBox
        leftSideBox.setStyle("-fx-spacing: 15px;");
        declineItemBox.setStyle("-fx-spacing: 10px;");
    }


    public void refreshTableContent(ArrayList<Item> items) {
        if (items == null) {
            Response<ArrayList<Item>> requestedItemsResponse = itemController.getRequestedItems();

            if (!requestedItemsResponse.getIsSuccess()) {
                MainViewController.getInstance(null).showAlert(
                        requestedItemsResponse.getIsSuccess(),
                        requestedItemsResponse.getIsSuccess() ? "Success" : "Error",
                        requestedItemsResponse.getMessage()
                );
                return;
            }

            items = requestedItemsResponse.getOutput();
        }

        itemsTable.getItems().clear();
        itemsTable.getItems().addAll(items);
    }

    // Helpers

    private Item getSelectedItem() {
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            MainViewController.getInstance(null).showAlert(
                    false,
                    "Error",
                    "Please select an item."
            );
            return null;
        }

        return selectedItem;
    }

    private void approveItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        Response<Integer> approveItemResponse = itemController.approveItem(item.getId());

        MainViewController.getInstance(null).showAlert(
                approveItemResponse.getIsSuccess(),
                approveItemResponse.getIsSuccess() ? "Success" : "Error",
                approveItemResponse.getMessage()
        );

        refreshTableContent(null);
    }

    private void declineItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        Response<Integer> declineItemResponse = itemController.declineItem(item.getId(), reasonField.getText());

        MainViewController.getInstance(null).showAlert(
                declineItemResponse.getIsSuccess(),
                declineItemResponse.getIsSuccess() ? "Success" : "Error",
                declineItemResponse.getMessage()
        );

        if (declineItemResponse.getIsSuccess()) {
            reasonField.setText("");
        }

        refreshTableContent(null);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == approveItemButton) {
            approveItem();
        }
        else if (evt.getSource() == declineItemButton) {
            declineItem();
        }
    }

}
