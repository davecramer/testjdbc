import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class TestInf {
    public static void main(String[] args) throws Exception {
        Properties connectionProperties =  new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(),"davec");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"password");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test", connectionProperties)) {
            try (PreparedStatement s = conn.prepareStatement("select real 'Inf', double precision 'Inf'")) {

                try (ResultSet rs = s.executeQuery()) {

                    while (rs.next()) {

                        System.out.println(rs.getFloat(1));

                        System.out.println(rs.getDouble(1));

                    }

                }

            }
        }

    }
}
