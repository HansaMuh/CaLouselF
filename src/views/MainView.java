package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view_controllers.MainViewController;

public class MainView {

    // Constructor

    public MainView(Stage stage) {
        init();
        setLayout();

        Scene scene = new Scene(viewBorder, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Properties

    private BorderPane viewBorder;
    private BorderPane topLevelBorder;

    private Button backButton;

    // Utilities

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
