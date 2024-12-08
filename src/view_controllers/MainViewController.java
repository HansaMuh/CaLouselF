package view_controllers;

import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import views.*;

import java.util.Stack;

public class MainViewController {

    // Constructor

    private MainViewController(Stage stage) {
        this.stage = stage;

        init();
    }

    // Static Fields

    private static MainViewController instance;

    // Properties

    private Stage stage;
    private MainView mainView;
    private Stack<VBox> pages;

    // Getters

    public static MainViewController getInstance(Stage stage) {
        if (instance == null && stage != null) {
            instance = new MainViewController(stage);
        }

        return instance;
    }

    // Utilities

    private void init() {
        mainView = new MainView(stage);
        pages = new Stack<>();
    }

    // Helper

    private boolean isViewMultilayered() {
        return pages.size() > 1;
    }

    private void refresh() {
        if (pages.lastElement() instanceof LoginView) {
            stage.setTitle("Log in | CaLouselF");
            stage.setWidth(450);
            stage.setHeight(300);

            mainView.setTopLevelBorder(false);
        }
        else if (pages.lastElement() instanceof RegisterView) {
            stage.setTitle("Register account | CaLouselF");
            stage.setWidth(450);
            stage.setHeight(450);

            mainView.setTopLevelBorder(true);
        }
        else if (pages.lastElement() instanceof HomeView) {
            stage.setTitle("Home | CaLouselF");
            stage.setWidth(825);
            stage.setHeight(675);

            mainView.setTopLevelBorder(false);
        }
        else if (pages.lastElement() instanceof WishlistView) {
            stage.setTitle("Wishlist | CaLouselF");
            stage.setWidth(825);
            stage.setHeight(675);

            mainView.setTopLevelBorder(true);
        }
        else if (pages.lastElement() instanceof PurchaseHistoryView) {
            stage.setTitle("Purchase History | CaLouselF");
            stage.setWidth(825);
            stage.setHeight(675);

            mainView.setTopLevelBorder(true);
        }
        else {
            stage.setTitle("CaLouselF");
            stage.setWidth(800);
            stage.setHeight(600);
        }
    }

    public void showAlert(boolean isSuccess, String title, String message) {
        Alert.AlertType alertType = isSuccess ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // Methods

    public void navigateBack() {
        if (isViewMultilayered()) {
            pages.pop();
            mainView.getContainer().setCenter(pages.lastElement());
        }

        refresh();
    }

    public void navigateToLogin() {
        LoginView view = new LoginView();
        pages.add(view);

        mainView.getContainer().setCenter(view);

        refresh();
        stage.centerOnScreen();
    }

    public void navigateToRegister() {
        RegisterView view = new RegisterView();
        pages.add(view);

        mainView.getContainer().setCenter(view);

        refresh();
    }

    public void navigateToHome(User user) {
        HomeView view = new HomeView(user);
        pages.add(view);

        mainView.getContainer().setCenter(view);

        refresh();
    }

    public void navigateToWishlist(User user) {
        WishlistView view = new WishlistView(user);
        pages.add(view);

        mainView.getContainer().setCenter(view);

        refresh();
    }

    public void navigateToPurchaseHistory(User user) {
        PurchaseHistoryView view = new PurchaseHistoryView(user);
        pages.add(view);

        mainView.getContainer().setCenter(view);

        refresh();
    }

}
