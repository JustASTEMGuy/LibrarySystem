package test;

import dao.UserDAO;
import user.User;

// Test file for User Authentication (DAO - Data Access Object - Data Abstraction)
public class TestUserDAO {
    public static void main(String[] args) {
    
        User loggedIn = UserDAO.loginUser("melody123", "gayming123");
        if (loggedIn != null) {
            System.out.println(loggedIn);
            System.out.println(loggedIn.getPassword());
        }

        else {
            System.err.println("no ok");
        }

        User registerIn = UserDAO.registerUser("sam", "sam123", "admin", "sam@gmail.com");
        if (registerIn != null) {
            System.out.println("Success!!");
        }

        else {
            System.out.println("no shit");
        }
    }
}