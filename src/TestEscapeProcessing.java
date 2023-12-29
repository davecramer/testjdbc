import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;

public class TestEscapeProcessing {
    private static String host = "localhost";
    public static void main(String[] args) throws Exception {
        long startTime;
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(), "postgres");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(), "password");

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://" + host + "/postgres", connectionProperties)) {

                try (PreparedStatement s = conn.prepareStatement("show application_name")) {
                    s.setEscapeProcessing(false);
                    try (ResultSet rs = s.executeQuery()) {
                        while (rs.next()) {
                            System.out.println(rs.getString(1));
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
}
