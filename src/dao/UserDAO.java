package dao;

import config.DBConnection;
import user.Admin;
import user.Student;
import user.User;
import java.sql.*;

public class UserDAO {

    public static User loginUser(String username, String password) { // Return a User class type
        
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Record is found 
                String role = rs.getString("role");
                if (role.equalsIgnoreCase("admin")) {
                    System.out.println("It's an admin!");
                    return new Admin(username, password);
                }
                else if (role.equalsIgnoreCase("student")) {
                    return new Student(username, password);
                }
            }

        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            
        }
        return null;
    }

    public static boolean registerUser(String username, String password, String role) {
        String insert = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}
