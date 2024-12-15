package singleton;

import models.User;

/*
    Singleton class that holds the current user that is logged in.
    It uses the Singleton pattern to ensure that only one instance of the class is created.
    The class provides a method to set and get the current user.
 */
public class UserAuthenticator {

    // Constructor

    private UserAuthenticator() {
        this.currentUser = null;
    }

    // Fields

    private static UserAuthenticator instance;

    // Properties

    private User currentUser;

    // Getters

    public static UserAuthenticator getInstance() {
        if (instance == null) {
            synchronized (UserAuthenticator.class) {
                if (instance == null) {
                    instance = new UserAuthenticator();
                }
            }
        }

        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Setters

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

}
