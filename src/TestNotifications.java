import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestNotifications {
    private static final String url = "jdbc:postgresql://localhost/postgres?ssl=verify-ca";

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(url, "davec", "password")) {
            Statement stmt = conn.createStatement();
            try (ResultSet rs = stmt.executeQuery("select ssl_is_used()")){
                if (rs.next()){
                    System.out.println("SSL is: " + rs.getBoolean(1));
                }
            }
            stmt.execute("LISTEN some_channel");
            PGConnection pgConn = conn.unwrap(org.postgresql.PGConnection.class);
            PGNotification[] notifications = pgConn.getNotifications();
            System.out.println (notifications.toString());
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
