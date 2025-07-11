package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContext {
    
    protected Connection connection;
    protected PreparedStatement ps;
    protected ResultSet rs;
    
    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://DESKTOP-ERT18VC:1433;databaseName=4USER_Project1";
            String user = "sa";
            String password = "12345";
            connection = DriverManager.getConnection(url, user, password);
            
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error " + e.getMessage() + " at DBContext!!!");
            return null;
        }
    }
    
    public static void main(String[] args) {
        DBContext test = new DBContext();
        test.connection = test.getConnection();
        System.out.println(test.connection);
    }
}
