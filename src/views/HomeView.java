package views;

import controllers.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Item;
import models.User;
import modules.Response;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class HomeView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public HomeView(User currentUser) {
        this.currentUser = currentUser;

        init();
        setLayout();
    }

    // Properties

    private User currentUser;

    private Label titleLabel;
    private Label captionLabel;

    private HBox topLevelMenu;
    private VBox contentBox;
    private Button logoutButton;
    private TableView<Item> itemsTable;

    // Methods

    private void init() {
        titleLabel = new Label("Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel =
                new Label("Welcome, " + currentUser.getUsername() + "! You are currently logged in as a " + currentUser.getRole().toString().toLowerCase() + ".");
        captionLabel.setStyle("-fx-font-size: 14px;");

        topLevelMenu = new HBox();

        logoutButton = new Button("Log out");
        logoutButton.setOnAction(this);

        itemsTable = new TableView<>();
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

        switch (currentUser.getRole()) {
            case SELLER -> contentBox = getSellerContent();
            case BUYER -> contentBox = getBuyerContent();
            case ADMIN -> contentBox = getAdminContent();
        }
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        topLevelMenu.setSpacing(10);
        topLevelMenu.getChildren().add(logoutButton);

        getChildren().addAll(topLevelMenu, titleLabel, captionLabel, contentBox);
    }

    private VBox getSellerContent() {
        VBox sellerContent = new VBox();
        sellerContent.setSpacing(10);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);

        Button viewItemsButton = new Button("View my items");
        viewItemsButton.setOnAction(this);

        Button uploadItemButton = new Button("Upload new item");
        uploadItemButton.setOnAction(this);

        buttonGrid.add(viewItemsButton, 0, 0);
        buttonGrid.add(uploadItemButton, 1, 0);

        refreshAvailableItemsTable();

        sellerContent.getChildren().addAll(itemsTable, buttonGrid);

        return sellerContent;
    }

    private VBox getBuyerContent() {
        VBox buyerContent = new VBox();
        buyerContent.setSpacing(10);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);

        Button viewWishlistButton = new Button("View wishlist");
        viewWishlistButton.setOnAction(this);

        Button viewPurchaseHistoryButton = new Button("View purchase history");
        viewPurchaseHistoryButton.setOnAction(this);

        buttonGrid.add(viewWishlistButton, 0, 0);
        buttonGrid.add(viewPurchaseHistoryButton, 1, 0);

        refreshAvailableItemsTable();

        buyerContent.getChildren().addAll(itemsTable, buttonGrid);

        return buyerContent;
    }

    private VBox getAdminContent() {
        VBox adminContent = new VBox();
        adminContent.setSpacing(10);

        Label tipLabel = new Label("*) Listed items above are items requested by sellers. " +
                "You can select and then approve or decline an item from here.");

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);

        Button approveItemButton = new Button("Approve item");
        approveItemButton.setOnMouseClicked(evt -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                MainViewController.getInstance(null).showAlert(
                        false,
                        "Error",
                        "Please select an item to approve."
                );
                return;
            }

            Response<Item> approveItemResponse = ItemController.approveItem(selectedItem.getId());

            MainViewController.getInstance(null).showAlert(
                    approveItemResponse.getIsSuccess(),
                    approveItemResponse.getIsSuccess() ? "Success" : "Error",
                    approveItemResponse.getMessage()
            );

            refreshRequestedItemsTable();
        });

        TextField reasonField = new TextField();
        reasonField.setPrefWidth(200);
        reasonField.setPromptText("Reason for declining");

        Button declineItemButton = new Button("Decline item");
        declineItemButton.setOnMouseClicked(evt -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                MainViewController.getInstance(null).showAlert(
                        false,
                        "Error",
                        "Please select an item to decline."
                );
                return;
            }

            Response<Item> declineItemResponse = ItemController.declineItem(selectedItem.getId(), reasonField.getText());

            MainViewController.getInstance(null).showAlert(
                    declineItemResponse.getIsSuccess(),
                    declineItemResponse.getIsSuccess() ? "Success" : "Error",
                    declineItemResponse.getMessage()
            );

            reasonField.setText("");
            refreshRequestedItemsTable();
        });

        HBox declineItemBox = new HBox();
        declineItemBox.getChildren().addAll(declineItemButton, reasonField);

        buttonGrid.add(approveItemButton, 0, 0);
        buttonGrid.add(declineItemBox, 1, 0);

        refreshRequestedItemsTable();

        adminContent.getChildren().addAll(itemsTable, tipLabel, buttonGrid);

        return adminContent;
    }

    private void refreshAvailableItemsTable() {
        Response<ArrayList<Item>> availableItemsResponse = ItemController.getAvailableItems();

        if (!availableItemsResponse.getIsSuccess()) {
            System.out.println(availableItemsResponse.getMessage());
            return;
        }

        itemsTable.getItems().clear();
        itemsTable.getItems().addAll(availableItemsResponse.getOutput());
    }

    private void refreshRequestedItemsTable() {
        Response<ArrayList<Item>> requestedItemsResponse = ItemController.getRequestedItems();

        if (!requestedItemsResponse.getIsSuccess()) {
            System.out.println(requestedItemsResponse.getMessage());
            return;
        }

        itemsTable.getItems().clear();
        itemsTable.getItems().addAll(requestedItemsResponse.getOutput());
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt)
    {
        if (evt.getSource() == logoutButton)
        {
            MainViewController.getInstance(null).showAlert(true,
                    "Log out", "You have been logged out.");
            MainViewController.getInstance(null).navigateBack();
        }
    }

}


