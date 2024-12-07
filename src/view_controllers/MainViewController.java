package view_controllers;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    // Methods

    public void navigateBack() {
        if (isViewMultilayered())
        {
            pages.pop();
            mainView.getContainer().setCenter(pages.lastElement());
        }
    }

    public void navigateToLogin() {
        stage.setTitle("Log in | CaLouselF");
        stage.setWidth(450);
        stage.setHeight(300);

        LoginView view = new LoginView();
        pages.add(view);
        mainView.getContainer().setCenter(view);
    }

    public void navigateToRegister() {
//        RegisterView view = new RegisterView();
//        pages.add(view);
//        mainView.getContainer().setCenter(view);
    }

}
