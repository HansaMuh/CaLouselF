package views;

import controllers.ItemController;
import controllers.WishlistController;
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
import models.Wishlist;
import modules.Response;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class WishlistView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public WishlistView(User currentUser) {
        this.currentController = new WishlistController();
        this.currentUser = currentUser;

        init();
        setLayout();
        setTable();
    }

    // Properties

    private WishlistController currentController;
    private User currentUser;

    private BorderPane dashboardPane;
    private HBox bottomPane;

    private Label titleLabel;
    private Label captionLabel;

    private TableView<Item> wishlistTable;

    private Button removeButton;

    // Methods

    private void init() {
        dashboardPane = new BorderPane();
        bottomPane = new HBox();

        titleLabel = new Label("Buyer's Wishlist");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel =
                new Label("Here are the items you've added to your wishlist. You can remove them with the button below.");
        captionLabel.setStyle("-fx-font-size: 14px;");

        wishlistTable = new TableView<>();

        removeButton = new Button("Remove item from wishlist");
        removeButton.setOnAction(this);
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        dashboardPane.setLeft(captionLabel);

        bottomPane.setSpacing(10);
        bottomPane.getChildren().addAll(removeButton);

        getChildren().addAll(titleLabel, dashboardPane, wishlistTable, bottomPane);
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

        wishlistTable.getColumns().addAll(nameColumn, categoryColumn, sizeColumn, priceColumn);

        // Autofit columns
        wishlistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTableContent(null);
    }

    public void refreshTableContent(ArrayList<Item> items) {
        if (items == null) {
            Response<ArrayList<Item>> wishlistedItemsResponse = currentController.getWishlistedItems(currentUser.getId());

            if (!wishlistedItemsResponse.getIsSuccess()) {
                MainViewController.getInstance(null).showAlert(
                        wishlistedItemsResponse.getIsSuccess(),
                        wishlistedItemsResponse.getIsSuccess() ? "Success" : "Error",
                        wishlistedItemsResponse.getMessage()
                );
                return;
            }

            items = wishlistedItemsResponse.getOutput();
        }

        wishlistTable.getItems().clear();
        wishlistTable.getItems().addAll(items);
    }

    // Helpers

    private Item getSelectedItem() {
        Item selectedItem = wishlistTable.getSelectionModel().getSelectedItem();

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

    private void removeWishlistedItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        Response<Wishlist> removeWishlistResponse = currentController.removeWishlist(item.getId(), currentUser.getId());

        MainViewController.getInstance(null).showAlert(
                removeWishlistResponse.getIsSuccess(),
                removeWishlistResponse.getIsSuccess() ? "Success" : "Error",
                removeWishlistResponse.getMessage()
        );

        refreshTableContent(null);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == removeButton) {
            removeWishlistedItem();
        }
    }

}
