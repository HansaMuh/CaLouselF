package views.subviews;

import controllers.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Item;
import modules.Response;
import view_controllers.MainViewController;
import views.HomeView;

import java.util.ArrayList;

public class BuyerHomeSubview extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public BuyerHomeSubview(HomeView parentView) {
        this.parentView = parentView;

        init();
        setLayout();
        setTable();
    }

    // Properties

    private HomeView parentView;

    private TableView<Item> itemsTable;

    private BorderPane bottomPane;
    private HBox leftSideBox;
    private HBox rightSideBox;
    private HBox offerBox;

    private Button addToWishlistButton;
    private Button purchaseButton;
    private Button makeOfferButton;
    private TextField offerField;

    private Button viewWishlistButton;
    private Button viewPurchaseHistoryButton;

    // Methods

    private void init() {
        itemsTable = new TableView<>();

        bottomPane = new BorderPane();
        leftSideBox = new HBox();
        rightSideBox = new HBox();
        offerBox = new HBox();

        addToWishlistButton = new Button("Add to wishlist");
        addToWishlistButton.setOnAction(this);

        purchaseButton = new Button("Purchase item");
        purchaseButton.setOnAction(this);

        makeOfferButton = new Button("Make an offer");
        makeOfferButton.setOnAction(this);

        offerField = new TextField();

        viewWishlistButton = new Button("View wishlist");
        viewWishlistButton.setOnAction(this);

        viewPurchaseHistoryButton = new Button("View purchase history");
        viewPurchaseHistoryButton.setOnAction(this);
    }

    private void setLayout() {
        setSpacing(10);

        offerBox.getChildren().addAll(makeOfferButton, offerField);

        leftSideBox.setSpacing(10);
        leftSideBox.getChildren().addAll(addToWishlistButton, purchaseButton, offerBox);

        rightSideBox.setSpacing(10);
        rightSideBox.getChildren().addAll(viewWishlistButton, viewPurchaseHistoryButton);

        bottomPane.setLeft(leftSideBox);
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

    public void refreshTableContent(ArrayList<Item> items) {
        if (items == null) {
            Response<ArrayList<Item>> availableItemsResponse = ItemController.getAvailableItems();

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
        if (evt.getSource() == addToWishlistButton) {
            // TODO: Add item to wishlist
        }
        else if (evt.getSource() == purchaseButton) {
            // TODO: Purchase item
        }
        else if (evt.getSource() == makeOfferButton) {
            // TODO: Make an offer
        }
        else if (evt.getSource() == viewWishlistButton) {
            // TODO: View wishlist
        }
        else if (evt.getSource() == viewPurchaseHistoryButton) {
            // TODO: View purchase history
        }
    }

}