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

public class HomeView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public HomeView() {
        this.currentController = new ItemController();
        this.currentUser = UserAuthenticator.getInstance().getCurrentUser();

        init();
        setLayout();
        // TODO: setStyling(); // Uncomment kalau sudah ada metode setStyling
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
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        topLevelMenu.setSpacing(10);
        topLevelMenu.getChildren().add(logoutButton);

        dashboardPane.setLeft(captionLabel);
        dashboardPane.setRight(browseBox);

        browseBox.setSpacing(5);
        browseBox.getChildren().addAll(browseField, browseButton);

        getChildren().addAll(topLevelMenu, titleLabel, dashboardPane, currentSubview);
    }

    private void setStyling() {
        // TODO: Implement styling
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
    }

}


