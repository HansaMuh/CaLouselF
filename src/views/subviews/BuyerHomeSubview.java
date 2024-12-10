package views.subviews;

import controllers.ItemController;
import controllers.WishlistController;
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
import models.User;
import models.Wishlist;
import modules.Response;
import view_controllers.MainViewController;
import views.HomeView;

import java.util.ArrayList;

public class BuyerHomeSubview extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public BuyerHomeSubview(User currentUser) {
        this.itemController = new ItemController();
        this.wishlistController = new WishlistController();
        this.currentUser = currentUser;

        init();
        setLayout();
        setTable();
    }

    // Properties

    private ItemController itemController;
    private WishlistController wishlistController;
    private User currentUser;

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

    private void addToWishlist() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        Response<Wishlist> addToWishlistResponse = wishlistController.addWishlist(item.getId(), currentUser.getId());

        MainViewController.getInstance(null).showAlert(
                addToWishlistResponse.getIsSuccess(),
                addToWishlistResponse.getIsSuccess() ? "Success" : "Error",
                addToWishlistResponse.getMessage()
        );
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == addToWishlistButton) {
            addToWishlist();
        }
        else if (evt.getSource() == purchaseButton) {
            // TODO: Purchase item
        }
        else if (evt.getSource() == makeOfferButton) {
            // TODO: Make an offer
        }
        else if (evt.getSource() == viewWishlistButton) {
            MainViewController.getInstance(null).navigateToWishlist(currentUser);
        }
        else if (evt.getSource() == viewPurchaseHistoryButton) {
            MainViewController.getInstance(null).navigateToPurchaseHistory(currentUser);
        }
    }

}