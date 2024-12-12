package views.subviews;

import controllers.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import models.Item;
import modules.Response;
import view_controllers.MainViewController;
import views.seller.OffersView;
import views.seller.SellerItemsView;

import java.util.ArrayList;

public class SellerHomeSubview extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public SellerHomeSubview() {
        this.itemController = new ItemController();

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

    private TableView<Item> itemsTable;

    private BorderPane bottomPane;
    private HBox rightSideBox;

    private Button viewOffersButton;
    private Button viewItemsButton;

    // Methods

    private void init() {
        itemsTable = new TableView<>();

        bottomPane = new BorderPane();
        rightSideBox = new HBox();

        viewOffersButton = new Button("View offers");
        viewOffersButton.setOnAction(this);

        viewItemsButton = new Button("View my items");
        viewItemsButton.setOnAction(this);
    }

    private void setLayout() {
        setSpacing(10);

        rightSideBox.setSpacing(10);
        rightSideBox.getChildren().addAll(viewOffersButton, viewItemsButton);

        bottomPane.setRight(rightSideBox);

        getChildren().addAll(itemsTable, bottomPane);
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
        // TODO: Implement styling
    }

    public void refreshTableContent(ArrayList<Item> items) {
        if (items == null) {
            Response<ArrayList<Item>> availableItemsResponse = itemController.getAvailableItems();

            if (!availableItemsResponse.getIsSuccess()) {
                MainViewController.getInstance(null).showAlert(
                        availableItemsResponse.getIsSuccess(),
                        availableItemsResponse.getIsSuccess() ? "Success" : "Error",
                        availableItemsResponse.getMessage()
                );
                return;
            }

            items = availableItemsResponse.getOutput();
        }

        itemsTable.getItems().clear();
        itemsTable.getItems().addAll(items);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == viewOffersButton) {
            MainViewController.getInstance(null).navigateTo(OffersView.class);
        }
        else if (evt.getSource() == viewItemsButton) {
            MainViewController.getInstance(null).navigateTo(SellerItemsView.class);
        }
    }

}
