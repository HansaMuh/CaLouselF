package view_controllers;

import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.ViewInfo;
import views.*;
import views.buyer.PurchaseHistoryView;
import views.buyer.WishlistView;
import views.guest.LoginView;
import views.guest.RegisterView;
import views.seller.ItemEditorView;
import views.seller.ItemUploaderView;
import views.seller.OffersView;
import views.seller.SellerItemsView;

import java.util.HashMap;
import java.util.Stack;

/*
    MainViewController class is used to manage the navigation between different views on an application window.
    It uses the Singleton pattern to ensure that only one instance of the class is created.
 */
public class MainViewController {

    // Constructor

    private MainViewController(Stage stage) {
        this.stage = stage;

        init();
        setViews();
    }

    // Fields

    private static MainViewController instance;

    private final double DEFAULT_WIDTH = 800;
    private final double DEFAULT_HEIGHT = 600;
    private final double LOGIN_WIDTH = 450;
    private final double LOGIN_HEIGHT = 450;
    private final double FORM_VIEW_WIDTH = 450;
    private final double FORM_VIEW_HEIGHT = 600;
    private final double TABLE_VIEW_WIDTH = 1025;
    private final double TABLE_VIEW_HEIGHT = 675;

    // Properties

    private Stage stage;
    private MainView mainView;
    private Stack<VBox> pages;
    private HashMap<Class, ViewInfo> viewMap;

    // Getters

    public static MainViewController getInstance(Stage stage) {
        if (instance == null && stage != null) {
            instance = new MainViewController(stage);
        }

        return instance;
    }

    // Utilities

    /*
        Initializes the fields of the class.
     */
    private void init() {
        mainView = new MainView(stage);
        pages = new Stack<>();
        viewMap = new HashMap<>();
    }

    /*
        Sets the views that can be navigated to.
     */
    private void setViews() {
        viewMap.put(LoginView.class, new ViewInfo("Log In", LOGIN_WIDTH, LOGIN_HEIGHT, false));
        viewMap.put(RegisterView.class, new ViewInfo("Register Account", FORM_VIEW_WIDTH, FORM_VIEW_HEIGHT, true));
        viewMap.put(HomeView.class, new ViewInfo("Home", TABLE_VIEW_WIDTH, TABLE_VIEW_HEIGHT, false));
        viewMap.put(WishlistView.class, new ViewInfo("Wishlist", TABLE_VIEW_WIDTH, TABLE_VIEW_HEIGHT, true));
        viewMap.put(PurchaseHistoryView.class, new ViewInfo("Purchase History", TABLE_VIEW_WIDTH, TABLE_VIEW_HEIGHT, true));
        viewMap.put(ItemEditorView.class, new ViewInfo("Item Editor", FORM_VIEW_WIDTH, FORM_VIEW_HEIGHT, true));
        viewMap.put(ItemUploaderView.class, new ViewInfo("Item Uploader", FORM_VIEW_WIDTH, FORM_VIEW_HEIGHT, true));
        viewMap.put(OffersView.class, new ViewInfo("Offers", TABLE_VIEW_WIDTH, TABLE_VIEW_HEIGHT, true));
        viewMap.put(SellerItemsView.class, new ViewInfo("My Items", TABLE_VIEW_WIDTH, TABLE_VIEW_HEIGHT, true));
    }

    // Helper

    /*
        Checks if the view is multilayered (i.e. contains multiple pages in an instance).
     */
    private boolean isViewMultilayered() {
        return pages.size() > 1;
    }

    /*
        Refreshes the stage properties based on the latest page.
     */
    private void refresh() {
        ViewInfo info = viewMap.get(pages.lastElement().getClass());

        if (info != null) {
            stage.setTitle(info.getName() + " | CaLouselF");
            stage.setWidth(info.getWidth());
            stage.setHeight(info.getHeight());

            mainView.setTopLevelBorder(info.isBottomLevel());
        }
        else {
            stage.setTitle("CaLouselF");
            stage.setWidth(DEFAULT_WIDTH);
            stage.setHeight(DEFAULT_HEIGHT);
        }
    }

    // Methods

    /*
        Shows an alert dialog with the specified title and message.
     */
    public void showAlert(boolean isSuccess, String title, String message) {
        Alert.AlertType alertType = isSuccess ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /*
        Navigates to the specified view. Primarily used by the Back button.
     */
    public void navigateBack() {
        if (isViewMultilayered()) {
            pages.pop();
            mainView.getContainer().setCenter(pages.lastElement());
        }

        refresh();
    }

    /*
        Navigates to the specified view, by using its class and optional arguments (if needed for the constructor).
     */
    public void navigateTo(Class<? extends VBox> viewClass, Object... args) {
        try {
            VBox view = null;

            if (args.length == 0) {
                // Constructor without args
                view = viewClass.getDeclaredConstructor().newInstance();
            }
            else {
                // Constructor with args
                Class<?>[] argTypes = new Class[args.length];

                for (int i = 0; i < args.length; i++) {
                    argTypes[i] = args[i].getClass();
                }

                view = viewClass.getDeclaredConstructor(argTypes).newInstance(args);
            }

            pages.add(view);
            mainView.getContainer().setCenter(view);

            refresh();
            stage.centerOnScreen();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            showAlert(
                    false,
                    "Navigation Error",
                    "Failed to navigate to the view:\r\n" + ex.getMessage());
        }
    }

}
