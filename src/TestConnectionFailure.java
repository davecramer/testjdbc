import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;

public class TestConnectionFailure {
    public static void main(String[] args) throws Exception{
        Properties connectionProperties =  new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(),"testpgiam");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"password1");
        connectionProperties.setProperty(PGProperty.APPLICATION_NAME.getName(), "My App");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://database-1.cgnh50a2ovor.us-east-1.rds.amazonaws.com/testpgiam", connectionProperties)) {
            try (PreparedStatement s = conn.prepareStatement("show application_name")) {
                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                }
            }
        } catch (SQLException ex ) {
            ex.printStackTrace();
        }
    }
}
