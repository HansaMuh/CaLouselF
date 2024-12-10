package main;

import view_controllers.MainViewController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // Program

    public static void main(String[] args) {
        launch(args);
    }

    // Properties

    private Stage primaryStage;

    // Overrides
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CaLouselF");

        MainViewController viewController = MainViewController.getInstance(primaryStage);
        viewController.navigateToLogin();
    }

}





