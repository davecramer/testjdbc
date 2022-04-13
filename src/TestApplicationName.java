import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class TestApplicationName {
    public static void main(String[] args) throws Exception{
        Properties connectionProperties =  new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(),"davec");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"password");
        connectionProperties.setProperty(PGProperty.APPLICATION_NAME.getName(), "My App");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test", connectionProperties)) {
            try (PreparedStatement s = conn.prepareStatement("show application_name")) {
                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                }
            }
        }
    }
}
