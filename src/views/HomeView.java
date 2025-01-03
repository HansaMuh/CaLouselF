package views;

import controllers.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Item;
import models.User;
import modules.Response;
import singleton.UserAuthenticator;
import view_controllers.MainViewController;
import views.subviews.AdminHomeSubview;
import views.subviews.BuyerHomeSubview;
import views.subviews.SellerHomeSubview;

import java.util.ArrayList;

/*
    HomeView is used to display the main dashboard of the application.
    It contains a top-level menu, a title, a caption, a search bar, and a subview that is specific
    to the current user's role.
 */
public class HomeView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public HomeView() {
        this.currentController = new ItemController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        setStyling();
    }

    // Properties

    private ItemController currentController;
    private User currentUser;
    private VBox currentSubview;

    private Button logoutButton;

    private HBox topLevelMenu;
    private BorderPane dashboardPane;
    private HBox browseBox;

    private Label titleLabel;
    private Label captionLabel;
    private TextField browseField;
    private Button browseButton;
    private Button refreshButton;

    // Methods

    private void init() {
        switch (currentUser.getRole()) {
            case SELLER:
                currentSubview = new SellerHomeSubview();
                break;
            case BUYER:
                currentSubview = new BuyerHomeSubview();
                break;

            case ADMIN:
                currentSubview = new AdminHomeSubview();
                break;
        }

        logoutButton = new Button("Log out");
        logoutButton.setOnAction(this);

        topLevelMenu = new HBox();
        dashboardPane = new BorderPane();
        browseBox = new HBox();

        titleLabel = new Label("Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel =
                new Label("Welcome, " + currentUser.getUsername() + "! You are currently logged in as a " + currentUser.getRole().toString().toLowerCase() + ".");
        captionLabel.setStyle("-fx-font-size: 14px;");

        browseField = new TextField();
        browseField.setPromptText("Enter a keyword...");

        browseButton = new Button("Browse");
        browseButton.setOnAction(this);

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(this);
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        topLevelMenu.setSpacing(10);
        topLevelMenu.getChildren().add(logoutButton);

        dashboardPane.setLeft(captionLabel);
        dashboardPane.setRight(browseBox);

        browseBox.setSpacing(5);
        browseBox.getChildren().addAll(browseField, browseButton, refreshButton);

        getChildren().addAll(topLevelMenu, titleLabel, dashboardPane, currentSubview);
    }

    private void setStyling() {
        this.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px; -fx-spacing: 20px;");

        topLevelMenu.setStyle("-fx-background-color: #00796b; -fx-padding: 10px; -fx-alignment: center-right;");
        logoutButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));

        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #00796b;");

        captionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #616161; -fx-padding: 5px 0;");

        browseBox.setStyle("-fx-spacing: 10px;");
        browseField.setStyle("-fx-font-size: 14px; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5px; -fx-padding: 5px;");
        browseButton.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;");
        browseButton.setOnMouseEntered(e -> browseButton.setStyle("-fx-background-color: #0277bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));
        browseButton.setOnMouseExited(e -> browseButton.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));

        refreshButton.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;");
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle("-fx-background-color: #0277bd; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));

        dashboardPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 15px; -fx-border-color: #e0e0e0; -fx-border-width: 1px;");

        if (currentSubview != null) {
            currentSubview.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-border-radius: 5px;");
        }
    }

    // Helpers

    private void browseAvailableItems() {
        String keyword = browseField.getText();

        Response<ArrayList<Item>> browseItemsResponse = currentController.browseAvailableItems(keyword);

        if (!browseItemsResponse.getIsSuccess()) {
            MainViewController.getInstance(null).showAlert(
                    false,
                    "Error",
                    browseItemsResponse.getMessage()
            );
            return;
        }

        switch (currentUser.getRole()) {
            case SELLER:
                ((SellerHomeSubview) currentSubview).refreshTableContent(browseItemsResponse.getOutput());
                break;
            case BUYER:
                ((BuyerHomeSubview) currentSubview).refreshTableContent(browseItemsResponse.getOutput());
                break;
        }
    }

    private void browseRequestedItems() {
        String keyword = browseField.getText();

        Response<ArrayList<Item>> browseItemsResponse = currentController.browseRequestedItems(keyword);

        if (!browseItemsResponse.getIsSuccess()) {
            MainViewController.getInstance(null).showAlert(
                    false,
                    "Error",
                    browseItemsResponse.getMessage()
            );
            return;
        }

        ((AdminHomeSubview) currentSubview).refreshTableContent(browseItemsResponse.getOutput());
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt)
    {
        if (evt.getSource() == logoutButton) {
            MainViewController.getInstance(null).showAlert(true,
                    "Log out", "You have been logged out.");
            MainViewController.getInstance(null).navigateBack();
        }
        else if (evt.getSource() == browseButton) {
            switch (currentUser.getRole()) {
                case SELLER:
                case BUYER:
                    browseAvailableItems();
                    break;

                case ADMIN:
                    browseRequestedItems();
                    break;
            }
        }
        else if (evt.getSource() == refreshButton) {
            switch (currentUser.getRole()) {
                case SELLER:
                    ((SellerHomeSubview) currentSubview).refreshTableContent(null);
                    break;
                case BUYER:
                    ((BuyerHomeSubview) currentSubview).refreshTableContent(null);
                    break;

                case ADMIN:
                    ((AdminHomeSubview) currentSubview).refreshTableContent(null);
                    break;
            }
        }
    }

}


