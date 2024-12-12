package singleton;

import models.User;

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
