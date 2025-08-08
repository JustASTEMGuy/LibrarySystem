package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.time.LocalDate;
import java.sql.Date;
import javax.swing.JOptionPane;

import config.DBConnection;

import obj.Transactions;

public class TransDAO {
    public static boolean insertTransaction(int userID, int bookID, LocalDate borrowDate) {
        String sql = "INSERT INTO transactions (user_id, book_id, borrow_date) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            stmt.setInt(2, bookID);
            stmt.setDate(3, Date.valueOf(borrowDate)); 

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Transaction insert error: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<Transactions> fetchTransactions() {
        
        ArrayList<Transactions> transList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from transactions")) {

            while (rs.next()) {
                Date sqlReturnDate = rs.getDate("return_date");
                LocalDate returnDate = (sqlReturnDate != null) 
                                ? sqlReturnDate.toLocalDate() 
                                : null;
                Transactions trans = new Transactions(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("book_id"), rs.getDate("borrow_date").toLocalDate(), 
                returnDate, rs.getString("status"));
                transList.add(trans);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return transList;
    }

    public static ArrayList<Transactions> fetchTransactions(int userID) {
        
        ArrayList<Transactions> transList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from transactions WHERE user_id = \'" + userID + "\'")) {

            while (rs.next()) {
                Date sqlReturnDate = rs.getDate("return_date");
                LocalDate returnDate = (sqlReturnDate != null) 
                                ? sqlReturnDate.toLocalDate() 
                                : null;
                Transactions trans = new Transactions(rs.getInt("id"), rs.getInt("book_id"), rs.getDate("borrow_date").toLocalDate(), 
                returnDate, rs.getString("status"));
                transList.add(trans);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return transList;
    }

    public static int fetchTotalTransactions() {
        String sql = "SELECT COUNT(*) FROM transactions";
        int count = 0;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
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

    public static ArrayList<Transactions> sortTransactions(int index, boolean asc) {
        String columnName;

        switch (index) {
            case 0:
                columnName = "id";
                break;
                
            case 1:
                columnName = "user_id";
                break;
                
            case 2:
                columnName = "book_id";
                break;
                
            case 3:
                columnName = "borrow_date";
                break;
                
            case 4:
                columnName = "return_date";
                break;

            default:
                // If this line runs, there's an error lol ;)
                columnName = "";
                break;
        }

        String sql;
        if (asc == true) {
            sql = "SELECT * from transactions ORDER BY " + columnName;
        }

        else {
            sql = "SELECT * from transactions ORDER BY " + columnName + " DESC";
        }
        

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            ArrayList<Transactions> transList = new ArrayList<>();
            
            while (rs.next()) {
                Date sqlReturnDate = rs.getDate("return_date");
                LocalDate returnDate = (sqlReturnDate != null) 
                                ? sqlReturnDate.toLocalDate() 
                                : null;
                Transactions trans = new Transactions(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("book_id"), rs.getDate("borrow_date").toLocalDate(), 
                returnDate, rs.getString("status"));
                transList.add(trans);
            }

            return transList;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    // Overloading for Member's Transaction View
    public static ArrayList<Transactions> sortTransactions(int index, boolean asc, int userID) {
        String columnName;

        switch (index) {
            case 0:
                columnName = "id";
                break;
                
            case 1:
                columnName = "book_id";
                break;
                
            case 2:
                columnName = "borrow_date";
                break;
                
            case 3:
                columnName = "return_date";
                break;

            default:
                // If this line runs, there's an error lol ;)
                columnName = "";
                break;
        }

        String sql;
        if (asc == true) {
            sql = "SELECT * from transactions WHERE user_id = \'" + userID + "\' ORDER BY " + columnName;
        }

        else {
            sql = "SELECT * from transactions WHERE user_id = \'" + userID + "\' ORDER BY " + columnName + " DESC";
        }
        

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            ArrayList<Transactions> transList = new ArrayList<>();
            
            while (rs.next()) {
                Date sqlReturnDate = rs.getDate("return_date");
                LocalDate returnDate = (sqlReturnDate != null) 
                                ? sqlReturnDate.toLocalDate() 
                                : null;
                Transactions trans = new Transactions(rs.getInt("id"), rs.getInt("book_id"), rs.getDate("borrow_date").toLocalDate(), 
                returnDate, rs.getString("status"));
                transList.add(trans);
            }

            return transList;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    // Transactions Return Update
    public static boolean returnTransactions(Transactions trans, LocalDate returnDate) {
        String sql = "UPDATE transactions SET status = ?, return_date = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "Returned");
            stmt.setDate(2, Date.valueOf(returnDate));
            stmt.setInt(3, trans.getID());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Update Transactions Display through User Input
    public static ArrayList<Transactions> searchUpdate(String phrase) {
        String sql = "SELECT * from transactions WHERE id LIKE \'%" + phrase + "%\'";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            ArrayList<Transactions> transList = new ArrayList<>();
            
            while (rs.next()) {
                Date sqlReturnDate = rs.getDate("return_date");
                LocalDate returnDate = (sqlReturnDate != null) 
                                ? sqlReturnDate.toLocalDate() 
                                : null;
                Transactions trans = new Transactions(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("book_id"), rs.getDate("borrow_date").toLocalDate(), 
                returnDate, rs.getString("status"));
                transList.add(trans);
            }

            return transList;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static ArrayList<Transactions> searchUpdate(String phrase, int UserID) {
        String sql = "SELECT * from transactions WHERE user_id = \'" + UserID + "\' AND id LIKE \'%" + phrase + "%\'";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            ArrayList<Transactions> transList = new ArrayList<>();
            
            while (rs.next()) {
                Date sqlReturnDate = rs.getDate("return_date");
                LocalDate returnDate = (sqlReturnDate != null) 
                                ? sqlReturnDate.toLocalDate() 
                                : null;
                Transactions trans = new Transactions(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("book_id"), rs.getDate("borrow_date").toLocalDate(), 
                returnDate, rs.getString("status"));
                transList.add(trans);
            }

            return transList;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static int fetchTotalBookBorrowed(int userID) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND status = \"Borrowed\"";
        int count = 0;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
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

    public static int fetchTotalBookOverDue(int userID) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND status = \"Overdue\"";
        int count = 0;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
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
}
