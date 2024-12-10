package views;

import controllers.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.User;
import modules.Response;
import view_controllers.MainViewController;

public class LoginView extends VBox implements EventHandler<ActionEvent> {

    // Constructor

    public LoginView() {
        this.currentController = new UserController();

        init();
        setLayout();
    }

    // Properties

    private UserController currentController;

    private Label titleLabel;
    private Label captionLabel;

    private GridPane formGrid;
    private GridPane buttonGrid;

    private Label usernameLabel;
    private TextField usernameField;

    private Label passwordLabel;
    private PasswordField passwordField;

    private Button loginButton;
    private Button navigateToRegisterButton;

    // Methods

    private void init() {
        titleLabel = new Label("Log in");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel = new Label("Please log in to access the rest of the application.");
        captionLabel.setStyle("-fx-font-size: 14px;");

        formGrid = new GridPane();
        buttonGrid = new GridPane();

        usernameLabel = new Label("Username");
        usernameField = new TextField();

        passwordLabel = new Label("Password");
        passwordField = new PasswordField();

        loginButton = new Button("Log in");
        loginButton.setOnAction(this);

        navigateToRegisterButton = new Button("Register Account");
        navigateToRegisterButton.setOnAction(this);
    }

    private void setLayout() {
        setPadding(new Insets(10, 15, 10, 15));
        setSpacing(15);

        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(5, 0, 5, 0));

        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(passwordLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);

        usernameField.setPrefWidth(225);

        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);

        buttonGrid.add(loginButton, 0, 0);
        buttonGrid.add(navigateToRegisterButton, 1, 0);

        loginButton.setPrefWidth(75);
        navigateToRegisterButton.setPrefWidth(125);

        getChildren().addAll(titleLabel, captionLabel, formGrid, buttonGrid);
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt)
    {
        if (evt.getSource() == loginButton)
        {
            String loginUsername = usernameField.getText();
            String loginPassword = passwordField.getText();

            Response<User> loginResponse = currentController.login(loginUsername, loginPassword);

            MainViewController.getInstance(null).showAlert(loginResponse.getIsSuccess(),
                    "Log in", loginResponse.getMessage());

            if (loginResponse.getIsSuccess()) {
                MainViewController.getInstance(null).navigateToHome(loginResponse.getOutput());
            }
        }
        else if (evt.getSource() == navigateToRegisterButton)
        {
            MainViewController.getInstance(null).navigateToRegister();
        }
    }

}

