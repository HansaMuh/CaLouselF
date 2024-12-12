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
        // TODO: setStyling(); // Uncomment kalau sudah ada metode setStyling
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
        // TODO: Implement styling
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
