package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import config.DBConnection;
import javax.swing.JOptionPane;
import obj.Book;

public class BookDAO {
    
    // Fetch the unsorted books
    public static ArrayList<Book> fetchBooks() {
        
        ArrayList<Book> bookList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from books")) {

            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getInt("quantity"));
                bookList.add(book);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return bookList;
    }

    // Add Book into DB
    public static Book addBook(String title, String author, String genre, int quantity) {
        String insert = "INSERT INTO books (title, author, genre, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, genre);
            stmt.setInt(4, quantity);
            stmt.executeUpdate(); // Execute the insert statement
            
            JOptionPane.showMessageDialog(null, "Book added successfully!");

            return new Book(title, author, genre, quantity);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            
        }
        return null;
    }

    public static boolean borrowBook(int bookID) {
        String checkSql = "SELECT quantity FROM books WHERE id = ?";
        String updateSql = "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, bookID);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int qty = rs.getInt("quantity");
                if (qty > 0) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, bookID);
                        int updated = updateStmt.executeUpdate();
                        return updated > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Borrow failed: " + e.getMessage());
        }
        return false;
    }

    // Return Books Method
    public static boolean returnBook(int bookID) {
        String checkSql = "SELECT quantity FROM books WHERE id = ?";
        String updateSql = "UPDATE books SET quantity = quantity + 1 WHERE id = ? AND quantity > 0";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, bookID);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int qty = rs.getInt("quantity");
                if (qty > 0) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, bookID);
                        int updated = updateStmt.executeUpdate();
                        return updated > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Borrow failed: " + e.getMessage());
        }
        return false;
    }

    public static int fetchTotalBook() {
        String sql = "SELECT COUNT(*) FROM books";
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
    
    public static void deleteBook(int id) {
        String delete = "DELETE FROM books WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(delete)) {

        stmt.setInt(1, id);
        stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            
        }
    }

    // Update Book in DB
    public static boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, quantity = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getQuantity());
            stmt.setInt(5, book.getID());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static ArrayList<Book> searchUpdate(String phrase) {
        String sql = "SELECT * from books WHERE title LIKE \'%" + phrase + "%\'";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            ArrayList<Book> bookList = new ArrayList<>();

            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getInt("quantity"));
                bookList.add(book);
            }
            return bookList;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    // Sort Book Method
    public static ArrayList<Book> sortBook(int index, boolean asc) {
        String columnName;

        switch (index) {
            case 0:
                columnName = "id";
                break;
                
            case 1:
                columnName = "title";
                break;
                
            case 2:
                columnName = "author";
                break;
                
            case 3:
                columnName = "genre";
                break;
                
            case 4:
                columnName = "quantity";
                break;

            default:
                // If this line runs, there's an error lol ;)
                columnName = "";
                break;
        }

        String sql;
        if (asc == true) {
            sql = "SELECT * from books ORDER BY " + columnName;
        }

        else {
            sql = "SELECT * from books ORDER BY " + columnName + " DESC";
        }
        

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            ArrayList<Book> bookList = new ArrayList<>();

            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getInt("quantity"));
                bookList.add(book);
            }

            return bookList;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }
    
}