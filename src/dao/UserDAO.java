package dao;

import config.DBConnection;
import user.Admin;
import user.Student;
import user.User;
import java.sql.*;
import javax.security.auth.x500.X500Principal;

public class UserDAO {

    public static User loginUser(String username, String password, String role) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Record is found 
                if (role.equals("admin")) {
                    return new Admin(username, password);
                }
                else if (role.equals("student")) {
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
