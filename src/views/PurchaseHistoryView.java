package views;

import controllers.TransactionController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Item;
import models.TransactionalItem;
import models.User;
import modules.Response;
import view_controllers.MainViewController;

import java.util.ArrayList;

public class PurchaseHistoryView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public PurchaseHistoryView(User currentUser) {
        this.currentController = new TransactionController();
        this.currentUser = currentUser;

        init();
        setLayout();
        setTable();
    }

    // Properties

    private TransactionController currentController;
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

    public void refreshTableContent(ArrayList<TransactionalItem> items) {
        if (items == null) {
            Response<ArrayList<TransactionalItem>> transactionalItemsResponse =
                    currentController.getItemsByTransaction(currentUser.getId());

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