import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestBadSSL {
    private final String url = "jdbc:postgresql://localhost:5432/test?" +
                "sslmode=verify-ca&sslcert=/Users/davecra/projects/jdbc/testjdbc/src/resources/goodclient.crt&" +
                "sslkey=/Users/davecra/projects/jdbc/testjdbc/src/resources/goodclient.p12&" + "sslrootcert=/Users/davecra/projects/jdbc/testjdbc/src/resources/goodroot.crt";

    private final String USER = "test";
    private final String PASSWORD = "test";


    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void main(String[] args) {

        TestBadSSL db = new TestBadSSL();
        db.connect();

    }

}
