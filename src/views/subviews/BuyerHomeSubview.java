package views.subviews;

import controllers.ItemController;
import controllers.OfferController;
import controllers.TransactionController;
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
import singleton.UserAuthenticator;
import view_controllers.MainViewController;
import views.HomeView;
import views.buyer.PurchaseHistoryView;
import views.buyer.WishlistView;

import java.util.ArrayList;

public class BuyerHomeSubview extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public BuyerHomeSubview() {
        this.itemController = new ItemController();
        this.wishlistController = new WishlistController();
        this.transactionController = new TransactionController();
        this.offerController = new OfferController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        setTable();
        // TODO: setStyling(); // Uncomment kalau sudah ada metode setStyling
    }

    // Properties

    private ItemController itemController;
    private WishlistController wishlistController;
    private TransactionController transactionController;
    private OfferController offerController;
    private User currentUser;

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

        Response<Integer> addToWishlistResponse = wishlistController.addWishlist(item.getId(), currentUser.getId());

        MainViewController.getInstance(null).showAlert(
                addToWishlistResponse.getIsSuccess(),
                addToWishlistResponse.getIsSuccess() ? "Success" : "Error",
                addToWishlistResponse.getMessage()
        );

        refreshTableContent(null);
    }

    private void purchaseItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        Response<Integer> purchaseResponse = transactionController.purchaseItem(currentUser.getId(), item.getId());

        MainViewController.getInstance(null).showAlert(
                purchaseResponse.getIsSuccess(),
                purchaseResponse.getIsSuccess() ? "Success" : "Error",
                purchaseResponse.getMessage()
        );

        refreshTableContent(null);
    }

    private void makeOffer() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        String offeredPrice = offerField.getText();
        Response<Integer> makeOfferResponse = offerController.makeOffer(currentUser.getId(), item.getId(), offeredPrice);

        MainViewController.getInstance(null).showAlert(
                makeOfferResponse.getIsSuccess(),
                makeOfferResponse.getIsSuccess() ? "Success" : "Error",
                makeOfferResponse.getMessage()
        );

        if (makeOfferResponse.getIsSuccess()) {
            offerField.setText("");
        }

        refreshTableContent(null);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == addToWishlistButton) {
            addToWishlist();
        }
        else if (evt.getSource() == purchaseButton) {
            purchaseItem();
        }
        else if (evt.getSource() == makeOfferButton) {
            makeOffer();
        }
        else if (evt.getSource() == viewWishlistButton) {
            MainViewController.getInstance(null).navigateTo(WishlistView.class);
        }
        else if (evt.getSource() == viewPurchaseHistoryButton) {
            MainViewController.getInstance(null).navigateTo(PurchaseHistoryView.class);
        }
    }

}