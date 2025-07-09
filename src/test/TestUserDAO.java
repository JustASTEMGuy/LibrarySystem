package test;

import dao.UserDAO;

public class TestUserDAO {
    public static void main(String[] args) {
        // login test..

        UserDAO userDAO = new UserDAO();
        boolean loggedIn = userDAO.loginUser("melody123", "gayming123", "student");
        System.out.println(loggedIn ? "Melody123 successful" : "Login failed");

        // Register test..
        boolean registered = UserDAO.registerUser("sam", "sam123", "admin");
        System.out.println(registered ? "Registration successful" : "Registration failed");


    }
}
