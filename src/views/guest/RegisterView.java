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

        sellerRadio.setSelected(true);

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

    // Helpers

    private void register() {
        String registerUsername = usernameField.getText();
        String registerPassword = passwordField.getText();
        String registerPhone = phoneField.getText();
        String registerAddress = addressField.getText();
        String registerRole = sellerRadio.isSelected() ? "Seller" : "Buyer";

        Response<Integer> registerResponse = userController.register(registerUsername, registerPassword, registerPhone,
                registerAddress,
                registerRole);

        MainViewController.getInstance(null).showAlert(registerResponse.getIsSuccess(),
                "Register account", registerResponse.getMessage());

        if (registerResponse.getIsSuccess()) {
            MainViewController.getInstance(null).navigateBack();
        }
    }

    // Overrides

    @Override
    public void handle(ActionEvent evt)
    {
        if (evt.getSource() == registerButton)
        {
            register();
        }
    }

}