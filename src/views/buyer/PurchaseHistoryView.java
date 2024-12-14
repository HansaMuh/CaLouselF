package views.buyer;

import controllers.TransactionController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.TransactionalItem;
import models.User;
import modules.Response;
import singleton.UserAuthenticator;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class PurchaseHistoryView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public PurchaseHistoryView() {
        this.transactionController = new TransactionController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        setTable();
        setStyling(); 
    }

    // Properties

    private TransactionController transactionController;
    private User currentUser;

    private BorderPane dashboardPane;
    private HBox bottomPane;

    private Label titleLabel;
    private Label captionLabel;

    private TableView<TransactionalItem> transactionalItemsTable;

    // Methods

    private void init() {
        dashboardPane = new BorderPane();
        bottomPane = new HBox();

        titleLabel = new Label("Buyer's Purchase History");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel =
                new Label("Here are the items you've purchased based on your transactions.");
        captionLabel.setStyle("-fx-font-size: 14px;");

        transactionalItemsTable = new TableView<>();
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        dashboardPane.setLeft(captionLabel);

        bottomPane.setSpacing(10);

        getChildren().addAll(titleLabel, dashboardPane, transactionalItemsTable, bottomPane);
    }

    private void setTable() {
        TableColumn<TransactionalItem, String> transactionIdColumn = new TableColumn<>("Transaction ID");
        TableColumn<TransactionalItem, String> nameColumn = new TableColumn<>("Name");
        TableColumn<TransactionalItem, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<TransactionalItem, String> sizeColumn = new TableColumn<>("Size");
        TableColumn<TransactionalItem, Double> priceColumn = new TableColumn<>("Price (IDR)");

        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<TransactionalItem, String>("transactionId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<TransactionalItem, String>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<TransactionalItem, String>("category"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<TransactionalItem, String>("size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<TransactionalItem, Double>("price"));

        transactionalItemsTable.getColumns().addAll(transactionIdColumn, nameColumn, categoryColumn, sizeColumn,
                priceColumn);

        // Autofit columns
        transactionalItemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTableContent(null);
    }

    private void setStyling() {
        // Main layout styling (VBox)
        setStyle("-fx-background-color: #f9f9f9; -fx-padding: 20px; -fx-spacing: 15px;");

        // Title Label styling
        titleLabel.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #34495e; " +  // Dark blue-gray for title
            "-fx-padding: 10px 0;"
        );

        // Caption Label styling
        captionLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #7f8c8d; " +  // Soft gray for caption text
            "-fx-padding: 5px 0;"
        );

        // Styling for the TableView
        transactionalItemsTable.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 8px; " +
            "-fx-border-color: #dfe6e9; " +  // Light gray border
            "-fx-border-width: 1px; " +
            "-fx-padding: 10px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2c3e50;"  // Dark blue-gray for text
        );

        // Styling Table Rows for Alternating Colors
        transactionalItemsTable.setRowFactory(tv -> {
            TableRow<TransactionalItem> row = new TableRow<>() {
                @Override
                protected void updateItem(TransactionalItem item, boolean empty) {
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
        for (TableColumn<TransactionalItem, ?> column : transactionalItemsTable.getColumns()) {
            column.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e; -fx-font-size: 14px;");
        }
    }


    public void refreshTableContent(ArrayList<TransactionalItem> items) {
        if (items == null) {
            Response<ArrayList<TransactionalItem>> transactionalItemsResponse =
                    transactionController.getItemsByTransaction(currentUser.getId());

            if (!transactionalItemsResponse.getIsSuccess()) {
                MainViewController.getInstance(null).showAlert(
                        transactionalItemsResponse.getIsSuccess(),
                        transactionalItemsResponse.getIsSuccess() ? "Success" : "Error",
                        transactionalItemsResponse.getMessage()
                );
                return;
            }

            items = transactionalItemsResponse.getOutput();
        }

        transactionalItemsTable.getItems().clear();
        transactionalItemsTable.getItems().addAll(items);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        // Empty
    }

}