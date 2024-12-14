package views.buyer;

import controllers.WishlistController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

public class WishlistView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public WishlistView() {
        this.wishlistController = new WishlistController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        setTable();
        setStyling(); 
    }

    // Properties

    private WishlistController wishlistController;
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

    private void setStyling() {
        // Main layout styling (VBox)
        setStyle("-fx-background-color: #f9f9f9; -fx-padding: 20px; -fx-spacing: 15px;");

        // Title Label styling
        titleLabel.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #2c3e50; " +  // Dark blue-gray color for title
            "-fx-padding: 10px 0;"
        );

        // Caption Label styling
        captionLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #7f8c8d; " +  // Soft gray for caption text
            "-fx-padding: 5px 0;"
        );

        // Styling for the TableView
        wishlistTable.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 8px; " +
            "-fx-border-color: #dfe6e9; " +  // Light gray border
            "-fx-border-width: 1px; " +
            "-fx-padding: 10px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2c3e50;"  // Dark blue-gray for text
        );

        // Styling Table Rows for Alternating Colors
        wishlistTable.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>() {
                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("-fx-background-color: white;"); // Default white
                    } else if (isSelected()) {
                        setStyle("-fx-background-color: #d1f2eb;"); // Light green for selected row
                    } else if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #f8f9fa;"); // Slightly gray for alternating rows
                    } else {
                        setStyle("-fx-background-color: white;"); // Pure white for default rows
                    }
                }
            };
            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #d1f2eb;")); // Hover effect
            row.setOnMouseExited(e -> row.setStyle("")); // Reset on hover exit
            return row;
        });

        // Styling TableView Columns
        for (TableColumn<Item, ?> column : wishlistTable.getColumns()) {
            column.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e; -fx-font-size: 14px;");
        }

        // Remove Button styling
        removeButton.setStyle(
            "-fx-background-color: #e74c3c; " +  // Red color for remove button
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-border-radius: 5px; " +
            "-fx-cursor: hand;"
        );

        // Button hover effect
        removeButton.setOnMouseEntered(e -> 
            removeButton.setStyle(
                "-fx-background-color: #c0392b; " +  // Darker red on hover
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );

        removeButton.setOnMouseExited(e -> 
            removeButton.setStyle(
                "-fx-background-color: #e74c3c; " +  // Revert to original color
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );

        // Button click effect
        removeButton.setOnMousePressed(e -> 
            removeButton.setStyle(
                "-fx-background-color: #c0392b; " +  // Darker red when clicked
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );

        removeButton.setOnMouseReleased(e -> 
            removeButton.setStyle(
                "-fx-background-color: #e74c3c; " +  // Revert to original color after release
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );
    }


    public void refreshTableContent(ArrayList<Item> items) {
        if (items == null) {
            Response<ArrayList<Item>> wishlistedItemsResponse = wishlistController.getWishlistedItems(currentUser.getId());

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

        Response<Integer> removeWishlistResponse = wishlistController.removeWishlistsByUser(item.getId(), currentUser.getId());

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
