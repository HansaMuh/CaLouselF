package views.seller;

import controllers.OfferController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.OfferedItem;
import models.User;
import modules.Response;
import singleton.UserAuthenticator;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class OffersView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public OffersView() {
        this.offerController = new OfferController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        setTable();
        setStyling(); 
    }

    // Properties

    private OfferController offerController;
    private User currentUser;

    private BorderPane dashboardPane;
    private HBox bottomPane;
    private HBox declineOfferBox;

    private Label titleLabel;
    private Label captionLabel;

    private TableView<OfferedItem> offeredItemsTable;

    private Button acceptOfferButton;
    private Button declineOfferButton;
    private TextField reasonField;

    // Methods

    private void init() {
        dashboardPane = new BorderPane();
        bottomPane = new HBox();
        declineOfferBox = new HBox();

        titleLabel = new Label("Seller's Offers");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel =
                new Label("Here are the items you've been offered. You can accept or decline them below.");
        captionLabel.setStyle("-fx-font-size: 14px;");

        offeredItemsTable = new TableView<>();

        acceptOfferButton = new Button("Accept offer");
        acceptOfferButton.setOnAction(this);

        declineOfferButton = new Button("Decline offer");
        declineOfferButton.setOnAction(this);

        reasonField = new TextField();
        reasonField.setPromptText("Reason for declining offer");
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        dashboardPane.setLeft(captionLabel);

        bottomPane.setSpacing(10);
        bottomPane.getChildren().addAll(acceptOfferButton, declineOfferBox);

        declineOfferBox.getChildren().addAll(declineOfferButton, reasonField);

        getChildren().addAll(titleLabel, dashboardPane, offeredItemsTable, bottomPane);
    }

    private void setTable() {
        TableColumn<OfferedItem, String> nameColumn = new TableColumn<>("Name");
        TableColumn<OfferedItem, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<OfferedItem, String> sizeColumn = new TableColumn<>("Size");
        TableColumn<OfferedItem, Double> priceColumn = new TableColumn<>("Initial Price (IDR)");
        TableColumn<OfferedItem, Double> offeredPriceColumn = new TableColumn<>("Offered Price (IDR)");

        nameColumn.setCellValueFactory(new PropertyValueFactory<OfferedItem, String>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<OfferedItem, String>("category"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<OfferedItem, String>("size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<OfferedItem, Double>("price"));
        offeredPriceColumn.setCellValueFactory(new PropertyValueFactory<OfferedItem, Double>("offeredPrice"));

        offeredItemsTable.getColumns().addAll(nameColumn, categoryColumn, sizeColumn, priceColumn, offeredPriceColumn);

        // Autofit columns
        offeredItemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTableContent(null);
    }

    private void setStyling() {
        // Main VBox styling
        setStyle("-fx-background-color: #f9f9f9; -fx-padding: 20px; -fx-spacing: 15px;");

        // Title Label styling
        titleLabel.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #34495e; " +  // Dark title color
            "-fx-padding: 10px 0;"
        );

        // Caption Label styling
        captionLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #7f8c8d; " +  // Subtle gray for captions
            "-fx-padding: 5px 0;"
        );

        // TableView styling
        offeredItemsTable.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #bdc3c7; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 5px; " +
            "-fx-padding: 10px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2c3e50;"
        );

        // Table Column Header styling
        for (TableColumn<OfferedItem, ?> column : offeredItemsTable.getColumns()) {
            column.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e; -fx-alignment: CENTER;");
        }

        // Alternating row colors
        offeredItemsTable.setRowFactory(tv -> {
            TableRow<OfferedItem> row = new TableRow<>() {
                @Override
                protected void updateItem(OfferedItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle("-fx-background-color: white;");
                    } else {
                        if (isSelected()) {
                            setStyle("-fx-background-color: #ecf0f1;");  // Light gray for selected
                        } else {
                            setStyle("-fx-background-color: #ffffff;");  // White for regular
                        }
                    }
                }
            };

            // Hover effect for rows
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #ecf0f1;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: white;"));
            return row;
        });

        // Bottom pane styling
        bottomPane.setStyle("-fx-spacing: 10px;");

        // Accept Offer Button styling
        acceptOfferButton.setStyle(
            "-fx-background-color: #27ae60; " +  // Green button
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        );
        acceptOfferButton.setOnMouseEntered(e -> acceptOfferButton.setStyle(
            "-fx-background-color: #229954; " +  // Darker green hover
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        ));
        acceptOfferButton.setOnMouseExited(e -> acceptOfferButton.setStyle(
            "-fx-background-color: #27ae60; " +  // Revert to original green
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        ));

        // Decline Offer Button styling
        declineOfferButton.setStyle(
            "-fx-background-color: #e74c3c; " +  // Red button
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        );
        declineOfferButton.setOnMouseEntered(e -> declineOfferButton.setStyle(
            "-fx-background-color: #c0392b; " +  // Darker red hover
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        ));
        declineOfferButton.setOnMouseExited(e -> declineOfferButton.setStyle(
            "-fx-background-color: #e74c3c; " +  // Revert to original red
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        ));

        // Reason Field styling
        reasonField.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #bdc3c7; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 5px; " +
            "-fx-padding: 5px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2c3e50;"
        );
    }


    public void refreshTableContent(ArrayList<OfferedItem> items) {
        if (items == null) {
            Response<ArrayList<OfferedItem>> response = offerController.getOfferedItems(currentUser.getId());

            if (!response.getIsSuccess()) {
                MainViewController.getInstance(null).showAlert(
                        response.getIsSuccess(),
                        response.getIsSuccess() ? "Success" : "Error",
                        response.getMessage()
                );
                return;
            }

            items = response.getOutput();
        }

        offeredItemsTable.getItems().clear();
        offeredItemsTable.getItems().addAll(items);
    }

    // Helpers

    private OfferedItem getSelectedItem() {
        OfferedItem selectedItem = offeredItemsTable.getSelectionModel().getSelectedItem();

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

    private void acceptOffer() {
        OfferedItem item = getSelectedItem();

        if (item == null) {
            return;
        }

        Response<Integer> response = offerController.acceptOffer(item.getOfferId(), currentUser.getId(), item.getId());

        MainViewController.getInstance(null).showAlert(
                response.getIsSuccess(),
                response.getIsSuccess() ? "Success" : "Error",
                response.getMessage()
        );

        refreshTableContent(null);
    }

    private void declineOffer() {
        OfferedItem item = getSelectedItem();

        if (item == null) {
            return;
        }

        String reason = reasonField.getText();
        Response<Integer> response = offerController.declineOffer(item.getOfferId(), currentUser.getId(),
                item.getId(), reason);

        MainViewController.getInstance(null).showAlert(
                response.getIsSuccess(),
                response.getIsSuccess() ? "Success" : "Error",
                response.getMessage()
        );

        if (response.getIsSuccess()) {
            reasonField.setText("");
        }

        refreshTableContent(null);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == acceptOfferButton) {
            acceptOffer();
        }
        else if (evt.getSource() == declineOfferButton) {
            declineOffer();
        }
    }

}
