package view_controllers;

import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import views.LoginView;
import views.MainView;
import views.RegisterView;

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

    private void refreshStage() {
        if (pages.lastElement() instanceof LoginView) {
            stage.setTitle("Log in | CaLouselF");
            stage.setWidth(450);
            stage.setHeight(300);
        } else if (pages.lastElement() instanceof RegisterView) {
            stage.setTitle("Register account | CaLouselF");
            stage.setWidth(450);
            stage.setHeight(450);
        } else {
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
        pages.pop();

        mainView.getContainer().setCenter(pages.lastElement());
        mainView.setTopLevelBorder(isViewMultilayered());
        refreshStage();
    }

    public void navigateToLogin() {
        LoginView view = new LoginView();
        pages.add(view);

        mainView.getContainer().setCenter(view);
        mainView.setTopLevelBorder(false);

        refreshStage();
        stage.centerOnScreen();
    }

    public void navigateToRegister() {
        RegisterView view = new RegisterView();
        pages.add(view);

        mainView.getContainer().setCenter(view);
        mainView.setTopLevelBorder(true);

        refreshStage();
    }

    public void navigateToHome(User user) {

    }

}
