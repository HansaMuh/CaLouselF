package views.guest;

import controllers.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import modules.Response;
import view_controllers.MainViewController;

public class RegisterView extends VBox implements EventHandler<ActionEvent> {

    // Constructor
    public RegisterView() {
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

    private Label phoneLabel;
    private TextField phoneField;

    private Label addressLabel;
    private TextField addressField;

    private Label roleLabel;
    private ToggleGroup roleGroup;
    private RadioButton sellerRadio;
    private RadioButton buyerRadio;

    private Button registerButton;

    // Methods

    private void init() {
        titleLabel = new Label("Register account");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        captionLabel = new Label("Fill out the form below to create a CaLouselF account.");
        captionLabel.setStyle("-fx-font-size: 14px;");

        formGrid = new GridPane();
        buttonGrid = new GridPane();

        usernameLabel = new Label("Username");
        usernameField = new TextField();

        passwordLabel = new Label("Password");
        passwordField = new PasswordField();

        phoneLabel = new Label("Phone Number");
        phoneField = new TextField();

        addressLabel = new Label("Address");
        addressField = new TextField();

        roleLabel = new Label("Role");
        roleGroup = new ToggleGroup();

        sellerRadio = new RadioButton("Seller");
        sellerRadio.setToggleGroup(roleGroup);

        buyerRadio = new RadioButton("Buyer");
        buyerRadio.setToggleGroup(roleGroup);

        sellerRadio.setSelected(true); // Default selection

        registerButton = new Button("Register");
        registerButton.setOnAction(this);
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
        formGrid.add(phoneLabel, 0, 2);
        formGrid.add(phoneField, 1, 2);
        formGrid.add(addressLabel, 0, 3);
        formGrid.add(addressField, 1, 3);
        formGrid.add(roleLabel, 0, 4);
        formGrid.add(sellerRadio, 1, 4);
        formGrid.add(buyerRadio, 1, 5);

        usernameField.setPrefWidth(225);

        buttonGrid.add(registerButton, 0, 0);

        registerButton.setPrefWidth(100);

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

        // Styling for usernameField, passwordField, phoneField, and addressField
        usernameField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px; -fx-padding: 8px; -fx-font-size: 14px;");
        passwordField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px; -fx-padding: 8px; -fx-font-size: 14px;");
        phoneField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px; -fx-padding: 8px; -fx-font-size: 14px;");
        addressField.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 4px; -fx-padding: 8px; -fx-font-size: 14px;");

        // Styling for labels
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        phoneLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        addressLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555;");
        roleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        // Styling for radio buttons (Seller, Buyer)
        sellerRadio.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
        buyerRadio.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        // Styling for registerButton
        registerButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-border-radius: 4px; " +
                "-fx-padding: 10px; " +
                "-fx-font-weight: bold;");

        // Hover effects for registerButton
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(
                "-fx-background-color: #45a049; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-border-radius: 4px; " +
                "-fx-padding: 10px; " +
                "-fx-font-weight: bold;"));

        registerButton.setOnMouseExited(e -> registerButton.setStyle(
                "-fx-background-color: #4CAF50; " +
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

    private void register() {
        String registerUsername = usernameField.getText();
        String registerPassword = passwordField.getText();
        String registerPhone = phoneField.getText();
        String registerAddress = addressField.getText();
        String registerRole = sellerRadio.isSelected() ? "Seller" : "Buyer";

        Response<Integer> registerResponse = userController.register(registerUsername, registerPassword, registerPhone,
                registerAddress, registerRole);

        MainViewController.getInstance(null).showAlert(registerResponse.getIsSuccess(),
                "Register account", registerResponse.getMessage());

        if (registerResponse.getIsSuccess()) {
            MainViewController.getInstance(null).navigateBack();
        }
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt) {
        if (evt.getSource() == registerButton) {
            register();
        }
    }

}
