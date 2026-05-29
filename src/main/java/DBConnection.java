import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/studentdb";
    private static final String USERNAME = "postgres";
    // Change this value to your actual PostgreSQL password before running.
    private static final String PASSWORD = "admin123";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
