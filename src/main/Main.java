package main;

//File: main/Main.java

import view_controllers.LoginViewController;
import view_controllers.RegisterViewController;
import views.HomeView;
import controllers.HomeController;
import models.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
 private Stage primaryStage;

 @Override
 public void start(Stage primaryStage) {
     this.primaryStage = primaryStage;
     showLoginView();
 }

 private void showLoginView() {
     LoginViewController loginVC = new LoginViewController(this);

     // Buat tombol untuk navigasi ke Register View
     Button toRegister = new Button("Go to Register");
     toRegister.setOnAction(e -> showRegisterView());

     BorderPane layout = new BorderPane();
     layout.setCenter(loginVC.getView());
     layout.setBottom(toRegister);

     Scene scene = new Scene(layout, 400, 400);
     primaryStage.setTitle("CaLouselF - Login");
     primaryStage.setScene(scene);
     primaryStage.show();
 }

 private void showRegisterView() {
     RegisterViewController registerVC = new RegisterViewController();

     // Buat tombol untuk navigasi kembali ke Login View
     Button toLogin = new Button("Go to Login");
     toLogin.setOnAction(e -> showLoginView());

     BorderPane layout = new BorderPane();
     layout.setCenter(registerVC.getView());
     layout.setBottom(toLogin);

     Scene scene = new Scene(layout, 400, 500);
     primaryStage.setTitle("CaLouselF - Register");
     primaryStage.setScene(scene);
     primaryStage.show();
 }

 // Metode untuk menunjukkan Home View setelah login berhasil
 public void showHomeView(User user) {
     HomeView homeView = new HomeView(user.getRole());
     HomeController homeController = new HomeController(homeView, user.getRole(), user.getId(), primaryStage);

     Scene scene = new Scene(homeView.getView(), 800, 600);
     primaryStage.setTitle("CaLouselF - Home");
     primaryStage.setScene(scene);
     primaryStage.show();
 }

 public static void main(String[] args) {
     launch(args);
 }
}



