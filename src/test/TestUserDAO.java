package test;

import dao.UserDAO;
import user.User;

// Test file for User Authentication (DAO - Data Access Object - Data Abstraction)
public class TestUserDAO {
    public static void main(String[] args) {
    
        User loggedIn = UserDAO.loginUser("melody123", "gayming123", "student");
        if (loggedIn != null) {
            System.out.println(loggedIn);
        }

        else {
            System.err.println("no ok");
        }

        // Register test..
        // boolean registered = UserDAO.registerUser("sam", "sam123", "admin");
        //System.out.println(registered ? "Registration successful" : "Registration failed");


    }
}
