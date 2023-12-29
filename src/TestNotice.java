import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;

public class TestNotice {
    public static void main(String[] args) {
        Properties connectionProperties =  new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(),"davec");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"password");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/postgres", connectionProperties)) {
            try (PreparedStatement statement = conn.prepareStatement("select throw_notice()")){
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        System.err.println(rs.getString(1));
                    }
                }
                SQLWarning warning = statement.getWarnings();
                System.err.println(warning.getMessage());
            }

        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
