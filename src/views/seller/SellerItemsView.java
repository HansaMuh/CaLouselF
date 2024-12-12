package views.seller;

import controllers.ItemController;
import enums.ItemStatus;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Item;
import models.User;
import modules.Response;
import singleton.UserAuthenticator;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class SellerItemsView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public SellerItemsView() {
        this.itemController = new ItemController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        setTable();
        // TODO: setStyling(); // Uncomment kalau sudah ada metode setStyling

        // Check if the view gets added back to a container by adding an event listener
        // to the view's parent property
        parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refreshTableContent(null);
            }
        });
    }

    // Properties

    private ItemController itemController;
    private User currentUser;

    private BorderPane dashboardPane;
    private HBox bottomPane;

    private Label titleLabel;
    private Label captionLabel;

    private TableView<Item> sellerItemsTable;

    private Button uploadButton;
    private Button editButton;
    private Button deleteButton;

    // Methods

    private void init() {
        dashboardPane = new BorderPane();
        bottomPane = new HBox();

        titleLabel = new Label("Seller's Items");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel =
                new Label("Here are the items you've uploaded. You can upload, edit or delete them with the buttons below.");
        captionLabel.setStyle("-fx-font-size: 14px;");

        sellerItemsTable = new TableView<>();

        uploadButton = new Button("Go to item uploader");
        uploadButton.setOnAction(this);

        editButton = new Button("Edit item");
        editButton.setOnAction(this);

        deleteButton = new Button("Delete item");
        deleteButton.setOnAction(this);
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        dashboardPane.setLeft(captionLabel);

        bottomPane.setSpacing(10);
        bottomPane.getChildren().addAll(uploadButton, editButton, deleteButton);

        getChildren().addAll(titleLabel, dashboardPane, sellerItemsTable, bottomPane);
    }

    private void setTable() {
        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price (IDR)");
        TableColumn<Item, ItemStatus> statusColumn = new TableColumn<>("Status");
        TableColumn<Item, String> notesColumn = new TableColumn<>("Notes");

        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("category"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Item, ItemStatus>("status"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("note"));

        sellerItemsTable.getColumns().addAll(nameColumn, categoryColumn, sizeColumn, priceColumn, statusColumn, notesColumn);

        // Autofit columns
        sellerItemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setStyling() {
        // TODO: Implement styling
    }

    public void refreshTableContent(ArrayList<Item> items) {
        if (items == null) {
            Response<ArrayList<Item>> response = itemController.getSellerItems(currentUser.getId());

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

        sellerItemsTable.getItems().clear();
        sellerItemsTable.getItems().addAll(items);
    }

    // Helpers

    private Item getSelectedItem() {
        Item selectedItem = sellerItemsTable.getSelectionModel().getSelectedItem();

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

    private void editItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        MainViewController.getInstance(null).navigateTo(ItemEditorView.class, item);
    }

    private void deleteItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        Response<Integer> response = itemController.invalidateItem(item.getId());

        MainViewController.getInstance(null).showAlert(
                response.getIsSuccess(),
                response.getIsSuccess() ? "Success" : "Error",
                response.getMessage()
        );

        refreshTableContent(null);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == uploadButton) {
            MainViewController.getInstance(null).navigateTo(ItemUploaderView.class);
        }
        else if (evt.getSource() == editButton) {
            editItem();
        }
        else if (evt.getSource() == deleteButton) {
            deleteItem();
        }
    }

}