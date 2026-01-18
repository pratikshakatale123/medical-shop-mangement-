package m3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
   
    // *** CONNECTION DETAILS CONFIRMED TO BE WORKING WITH YOUR SETUP ***
    private static final String URL = "jdbc:mysql://localhost:3306/medicalshop"; 
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        // Your specific implementation using if (con == null) is also valid, 
        // but DriverManager.getConnection handles it simpler.
        try {
            // This line is often unnecessary with modern JDBC 4.0+, 
            // but is kept as it was in your previous version:
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
