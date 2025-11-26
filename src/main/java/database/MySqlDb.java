package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDb {

    private final String url = "jdbc:mysql://localhost:3306/java-give-my-vara";
    private final String user = "root";
    private final String pass = "";

    public Connection conn;  // public or use getter

    // Constructor opens the connection automatically
    public MySqlDb() {
        try {
            // Load MySQL driver
//            Class.forName("com.mysql.cj.jdbc.Driver");  
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            conn = null;
        }
    }

    // Optional getter if you want to keep 'conn' private
    public Connection getConnection() {
        return conn;
    }
}
