package controllers;


import view_controllers.UploadItemViewController;
import view_controllers.ViewItemsViewController;
import view_controllers.AdminViewController;
import view_controllers.BuyerViewController;
import view_controllers.WishlistViewController;
import view_controllers.PurchaseHistoryViewController;
import views.HomeView;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class HomeController {
    private HomeView view;
    private String role;
    private int userId; // ID pengguna yang sedang login

    public HomeController(HomeView view, String role, int userId, Stage primaryStage) {
        this.view = view;
        this.role = role;
        this.userId = userId;
        initialize(primaryStage);
    }

    private void initialize(Stage primaryStage) {
        Button uploadItemButton = view.getUploadItemButton();
        Button viewItemsButton = view.getViewItemsButton();
        Button logoutButton = view.getLogoutButton();
        Button viewWishlistButton = view.getViewWishlistButton();
        Button viewPurchaseHistoryButton = view.getViewPurchaseHistoryButton();

        if (role.equalsIgnoreCase("Seller")) {
            if (uploadItemButton != null) {
                uploadItemButton.setOnAction(e -> {
                    UploadItemViewController uploadVC = new UploadItemViewController(userId);
                    BorderPane layout = new BorderPane();
                    layout.setCenter(uploadVC.getView());

                    // Tambahkan tombol kembali ke Home
                    Button backButton = new Button("Back to Home");
                    backButton.setOnAction(event -> {
                        HomeView homeView = new HomeView(role);
                        HomeController homeController = new HomeController(homeView, role, userId, primaryStage);
                        layout.setCenter(homeView.getView());
                    });
                    layout.setTop(backButton);

                    Scene scene = new Scene(layout, 400, 400);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("CaLouselF - Upload Item");
                });
            }

            if (viewItemsButton != null) {
                viewItemsButton.setOnAction(e -> {
                    ViewItemsViewController viewItemsVC = new ViewItemsViewController(userId);
                    BorderPane layout = new BorderPane();
                    layout.setCenter(viewItemsVC.getView());

                    // Tambahkan tombol kembali ke Home
                    Button backButton = new Button("Back to Home");
                    backButton.setOnAction(event -> {
                        HomeView homeView = new HomeView(role);
                        HomeController homeController = new HomeController(homeView, role, userId, primaryStage);
                        layout.setCenter(homeView.getView());
                    });
                    layout.setTop(backButton);

                    Scene scene = new Scene(layout, 600, 400);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("CaLouselF - My Items");
                });
            }
        } else if (role.equalsIgnoreCase("Admin")) {
            // Inisialisasi AdminView
            AdminViewController adminVC = new AdminViewController();
            BorderPane layout = new BorderPane();
            layout.setCenter(adminVC.getView());

            Scene scene = new Scene(layout, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("CaLouselF - Admin Dashboard");
        } else if (role.equalsIgnoreCase("Buyer")) {
            if (viewWishlistButton != null) {
                viewWishlistButton.setOnAction(e -> {
                    WishlistViewController wishlistVC = new WishlistViewController(userId);
                    BorderPane layout = new BorderPane();
                    layout.setCenter(wishlistVC.getView());

                    // Tambahkan tombol kembali ke Home
                    Button backButton = new Button("Back to Home");
                    backButton.setOnAction(event -> {
                        HomeView homeView = new HomeView(role);
                        HomeController homeController = new HomeController(homeView, role, userId, primaryStage);
                        layout.setCenter(homeView.getView());
                    });
                    layout.setTop(backButton);

                    Scene scene = new Scene(layout, 600, 400);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("CaLouselF - Wishlist");
                });
            }

            if (viewPurchaseHistoryButton != null) {
                viewPurchaseHistoryButton.setOnAction(e -> {
                    PurchaseHistoryViewController purchaseHistoryVC = new PurchaseHistoryViewController(userId);
                    BorderPane layout = new BorderPane();
                    layout.setCenter(purchaseHistoryVC.getView());

                    // Tambahkan tombol kembali ke Home
                    Button backButton = new Button("Back to Home");
                    backButton.setOnAction(event -> {
                        HomeView homeView = new HomeView(role);
                        HomeController homeController = new HomeController(homeView, role, userId, primaryStage);
                        layout.setCenter(homeView.getView());
                    });
                    layout.setTop(backButton);

                    Scene scene = new Scene(layout, 800, 600);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("CaLouselF - Purchase History");
                });
            }

            // Implementasikan BuyerViewController untuk fitur utama buyer
            BuyerViewController buyerVC = new BuyerViewController(userId);
            BorderPane layout = new BorderPane();
            layout.setCenter(buyerVC.getView());

            // Tambahkan navigasi tambahan jika diperlukan

            Scene scene = new Scene(layout, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("CaLouselF - Buyer Dashboard");
        }

        logoutButton.setOnAction(e -> {
            // Kembali ke halaman login
            // Implementasikan logika logout sesuai kebutuhan Anda
            // Misalnya, memanggil metode di Main class untuk menunjukkan login view
            primaryStage.close();
            // Atau mengatur ulang scene ke login view seperti sebelumnya
            // Anda mungkin perlu mengakses referensi ke Main class
        });
    }
}


