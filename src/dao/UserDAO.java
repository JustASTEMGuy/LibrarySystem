package dao;

import config.DBConnection;
import obj.Book;
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

    public static User addUser(String username, String password, String email, boolean banned_status, boolean isStudent) {
        String insert = "INSERT INTO users (username, password, role, email, Banned_Status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            if(isStudent) {
                stmt.setString(3, "student");
            }
            else {
                stmt.setString(3, "admin");
            }
            
            stmt.setString(4, email);
            stmt.setBoolean(5, banned_status);
            stmt.executeUpdate(); // Execute the insert statement

            System.out.println("Registering user: " + username);

            if(isStudent) {
                return new Student(username, password, "student", email, false);
            }
            else {
                return new Student(username, password, "admin", email, false);
            }

            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            
        }
        return null;
    }

    public static int fetchTotalUser(String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        int count = 0;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

            return count;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    public static void deleteUser(int id) {
        String delete = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(delete)) {

        stmt.setInt(1, id);
        stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            
        }
    }

    public static boolean updateUser(User user, boolean isStudent) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, email = ?, Banned_Status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println(user.getID());
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            if (isStudent) {
                stmt.setString(3, "student");
            }
            else {
                stmt.setString(3, "admin");
            }
            
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.getBannedStatus());
            stmt.setInt(6, user.getID());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static ArrayList<Student> searchUpdate(String phrase, boolean isStudent) {
        String sql;
        if (isStudent) {
            sql = "SELECT * from users WHERE username LIKE \'%" + phrase + "%\' AND role = \'student\'";
        }
        else {
            sql = "SELECT * from users WHERE username LIKE \'%" + phrase + "%\' AND role = \'admin\'";
        }

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            ArrayList<Student> userList = new ArrayList<>();

            while (rs.next()) {
                Student user = new Student(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getBoolean("Banned_Status"));
                userList.add(user);
            }

            return userList;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static ArrayList<Student> sortUser(int index, boolean asc, boolean isStudent) {
        String columnName;

        switch (index) {
            case 0:
                columnName = "id";
                break;
                
            case 1:
                columnName = "username";
                break;
                
            case 2:
                columnName = "email";
                break;

            default:
                // If this line runs, there's an error lol ;)
                columnName = "";
                break;
        }

        String sql;
        if (asc == true) {
            if (isStudent) {
                sql = "SELECT * from users WHERE role = 'student' ORDER BY " + columnName;
            }
            else {
                sql = "SELECT * from users WHERE role = 'admin' ORDER BY " + columnName;
            }
        }

        else {
            if (isStudent) {
                sql = "SELECT * from users WHERE role = 'student' ORDER BY " + columnName + " DESC";
            }
            else {
                sql = "SELECT * from users WHERE role = 'admin' ORDER BY " + columnName + " DESC";
            }
        }
        

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            ArrayList<Student> userList = new ArrayList<>();

            while (rs.next()) {
                Student user = new Student(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getBoolean("Banned_Status"));
                userList.add(user);
            }

            return userList;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }
    
}
