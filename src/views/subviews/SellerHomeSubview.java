package views.subviews;

import controllers.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

/*
    SellerHomeSubview is used to display the home view for sellers.
    This displays a table of items that is still available for sale.
    Sellers can also view offers made by buyers and view their own items.
    It's the first view that sellers see when they log in.
 */
public class SellerHomeSubview extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public SellerHomeSubview() {
        this.itemController = new ItemController();

        init();
        setLayout();
        setTable();
        setStyling(); 

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
        setStyle("-fx-background-color: #f4f7fc; -fx-padding: 20px; -fx-spacing: 15px;");

        String buttonStyle =
            "-fx-background-color: #3f51b5; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;" +
            "-fx-cursor: hand;";

        viewOffersButton.setStyle(buttonStyle);
        viewItemsButton.setStyle(buttonStyle);

        viewOffersButton.setOnMouseEntered(e -> viewOffersButton.setStyle(buttonStyle + "-fx-background-color: #303f9f;"));
        viewOffersButton.setOnMouseExited(e -> viewOffersButton.setStyle(buttonStyle));

        viewItemsButton.setOnMouseEntered(e -> viewItemsButton.setStyle(buttonStyle + "-fx-background-color: #303f9f;"));
        viewItemsButton.setOnMouseExited(e -> viewItemsButton.setStyle(buttonStyle));

        itemsTable.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #dcdcdc; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2c3e50;"
        );

        itemsTable.setRowFactory(tv -> {
            return new TableRow<Item>() {
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
            };
        });

        for (TableColumn<Item, ?> column : itemsTable.getColumns()) {
            column.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #34495e;");
        }

        bottomPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px; -fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px 0 0 0; -fx-border-radius: 5px;");
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
