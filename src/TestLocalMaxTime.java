import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

public class TestLocalMaxTime {

    public static void main(String[] args) throws Exception {

        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "dave");
        try (Connection conn = DriverManager.getConnection(url, props)) {
            try (PreparedStatement stmt = conn.prepareStatement("select ts from tstamp where ts = ?")) {
                stmt.setTimestamp(1, Timestamp.valueOf(LocalDate.of(2023, 2, 28).atTime(LocalTime.MAX)));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        System.out.println("Get String: " + rs.getString(1));
                }
            }
        }
    }
}