package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view_controllers.MainViewController;

/*
    MainView is used to represent the main view of the application.
    It is the first view that is shown when the application is started.
 */
public class MainView {

    // Constructor

    public MainView(Stage stage) {
        init();
        setLayout();
        setStyling();

        Scene scene = new Scene(viewBorder);
        stage.setScene(scene);
        stage.show();
    }

    // Properties

    private BorderPane viewBorder;
    private BorderPane topLevelBorder;

    private Button backButton;

    // Methods

    private void init() {
        viewBorder = new BorderPane();
        topLevelBorder = new BorderPane();

        backButton = new Button("Back");
        backButton.setOnMouseClicked(evt -> {
            MainViewController.getInstance(null).navigateBack();
        });
    }

    private void setLayout() {
        setTopLevelBorder(false);
        topLevelBorder.setLeft(backButton);
    }

    private void setStyling() {
        topLevelBorder.setStyle("-fx-background-color: #00796b; -fx-padding: 10px; -fx-alignment: center-right;");

        backButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 15px; -fx-border-radius: 5px;"));
    }

    // Helpers

    public void setTopLevelBorder(boolean isActive) {
        if (isActive) {
            viewBorder.setTop(topLevelBorder);
            BorderPane.setMargin(topLevelBorder, new Insets(10, 15, 10, 15));
        }
        else {
            viewBorder.setTop(null);
        }
    }

    // Getters

    public BorderPane getContainer() {
        return viewBorder;
    }

}
