package main;

import view_controllers.MainViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import views.guest.LoginView;

public class Main extends Application {

    // Program

    public static void main(String[] args) {
        launch(args);
    }

    // Properties

    private Stage primaryStage;

    // Overrides

    /*
    This method is called when the application is launched, where it creates the View system and starts
    by navigating to the Login view.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setWidth(450);
        this.primaryStage.setHeight(450);

        MainViewController viewController = MainViewController.getInstance(primaryStage);
        viewController.navigateTo(LoginView.class);
    }

}





