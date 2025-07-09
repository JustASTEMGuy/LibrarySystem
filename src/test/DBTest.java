package test;

import config.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection to database successful!");
            } else {
                System.out.println("Connection failed.");
            }

        } catch (SQLException e) {
            
            System.err.println("SQLException occurred:");

        }
    }
}