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
import models.User;
import modules.Response;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class AdminHomeSubview extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public AdminHomeSubview(User currentUser) {
        this.currentUser = currentUser;

        init();
        setLayout();
        setTable();
    }

    // Properties

    private User currentUser;

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

    public void refreshTableContent(ArrayList<Item> items) {
        if (items == null) {
            Response<ArrayList<Item>> requestedItemsResponse = ItemController.getRequestedItems();

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

        Response<Item> approveItemResponse = ItemController.approveItem(item.getId());

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

        Response<Item> declineItemResponse = ItemController.declineItem(item.getId(), reasonField.getText());

        MainViewController.getInstance(null).showAlert(
                declineItemResponse.getIsSuccess(),
                declineItemResponse.getIsSuccess() ? "Success" : "Error",
                declineItemResponse.getMessage()
        );

        reasonField.setText("");
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
