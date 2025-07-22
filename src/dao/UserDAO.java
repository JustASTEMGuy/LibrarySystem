package dao;

import config.DBConnection;

import user.Admin;
import user.Student;
import user.User;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
            JOptionPane.showMessageDialog(null, "Login error: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            
        }

        return null;
    }
    
    public static User registerUser(String username, String password, String role, String email) {
        String insert = "INSERT INTO users (username, password, role, email, Banned_Status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setString(4, email);
            stmt.setBoolean(5, false);
            stmt.executeUpdate(); // Execute the insert statement

            System.out.println("Registering user: " + username);
            return new Student(username, password, role, email, false);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            
        }
        return null;
    }

    public static ArrayList<Student> fetchUsers(String role) {

        ArrayList<Student> userList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from users WHERE role = \"" + role + "\"")) {
                
            while (rs.next()) {
                Student user = new Student(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getBoolean("Banned_Status"));
                userList.add(user);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return userList;
    }
}
