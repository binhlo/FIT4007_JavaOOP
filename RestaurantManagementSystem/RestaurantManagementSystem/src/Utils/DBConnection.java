package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection makeConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String username = "sa";
            String password = "12345678";
            String url = "jdbc:sqlserver://DELLG15-THANHBI\\DNU;databaseName=RestaurantManagementSystem;encrypt=true;trustServerCertificate=true;";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException thrownException) {
            ((Exception) thrownException).printStackTrace();
            return null;
        }

    }
}