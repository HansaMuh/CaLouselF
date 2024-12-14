package views.guest;

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
import singleton.UserAuthenticator;
import view_controllers.MainViewController;
import views.HomeView;

public class LoginView extends VBox implements EventHandler<ActionEvent> {

    // Constructor
    public LoginView() {
        this.userController = new UserController();

        init();
        setLayout();
        setStyling(); 
    }

    // Properties
    private UserController userController;

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

    private void setStyling() {
        // Styling for titleLabel
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4CAF50; -fx-margin-bottom: 10px;");

        // Styling for captionLabel
        captionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555; -fx-margin-bottom: 20px;");

        // Styling for formGrid
        formGrid.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 8px; -fx-padding: 15px;");
        formGrid.setVgap(15);
        formGrid.setHgap(10);

        // Styling for usernameField and passwordField
        usernameField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px; -fx-padding: 8px; -fx-font-size: 14px;");
        passwordField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px; -fx-padding: 8px; -fx-font-size: 14px;");
        
        // Styling for usernameLabel and passwordLabel
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        
        // Styling for buttonGrid
        buttonGrid.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 8px;");
        buttonGrid.setHgap(15);
        buttonGrid.setVgap(10);
        
        // Styling for loginButton
        loginButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-border-radius: 4px; " +
                "-fx-padding: 10px; " +
                "-fx-font-weight: bold;");
        
        // Styling for navigateToRegisterButton
        navigateToRegisterButton.setStyle(
                "-fx-background-color: #2196F3; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-border-radius: 4px; " +
                "-fx-padding: 10px; " +
                "-fx-font-weight: bold;");
        
        // Hover effects for buttons
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #45a049; " + 
                "-fx-text-fill: white; " + 
                "-fx-font-size: 14px; " + 
                "-fx-border-radius: 4px; " + 
                "-fx-padding: 10px; " + 
                "-fx-font-weight: bold;"));
        
        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: #4CAF50; " + 
                "-fx-text-fill: white; " + 
                "-fx-font-size: 14px; " + 
                "-fx-border-radius: 4px; " + 
                "-fx-padding: 10px; " + 
                "-fx-font-weight: bold;"));
        
        navigateToRegisterButton.setOnMouseEntered(e -> navigateToRegisterButton.setStyle(
                "-fx-background-color: #1976D2; " + 
                "-fx-text-fill: white; " + 
                "-fx-font-size: 14px; " + 
                "-fx-border-radius: 4px; " + 
                "-fx-padding: 10px; " + 
                "-fx-font-weight: bold;"));
        
        navigateToRegisterButton.setOnMouseExited(e -> navigateToRegisterButton.setStyle(
                "-fx-background-color: #2196F3; " + 
                "-fx-text-fill: white; " + 
                "-fx-font-size: 14px; " + 
                "-fx-border-radius: 4px; " + 
                "-fx-padding: 10px; " + 
                "-fx-font-weight: bold;"));
        
        // Additional background and layout styling for VBox
        setStyle("-fx-background-color: #eeeeee; -fx-padding: 25px; -fx-alignment: center;");
        setSpacing(20);
    }

    // Helpers
    private void login() {
        String loginUsername = usernameField.getText();
        String loginPassword = passwordField.getText();

        Response<User> loginResponse = userController.login(loginUsername, loginPassword);

        MainViewController.getInstance(null).showAlert(
                loginResponse.getIsSuccess(),
                loginResponse.getIsSuccess() ? "Success" : "Error",
                loginResponse.getMessage());

        if (loginResponse.getIsSuccess()) {
            UserAuthenticator.getInstance().setCurrentUser(loginResponse.getOutput());
            MainViewController.getInstance(null).navigateTo(HomeView.class);
        }
    }

    // Overrides
    @Override
    public void handle(ActionEvent evt)
    {
        if (evt.getSource() == loginButton) {
            login();
        }
        else if (evt.getSource() == navigateToRegisterButton) {
            MainViewController.getInstance(null).navigateTo(RegisterView.class);
        }
    }

}
