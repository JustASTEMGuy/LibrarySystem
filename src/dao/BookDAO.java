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
import user.Student;

public class BookDAO {
    
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
    
}
