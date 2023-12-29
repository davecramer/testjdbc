import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;

public class ConnectionOpenTime {
    private static String host = "database-1.cluster-cgnh50a2ovor.us-east-1.rds.amazonaws.com";
    public static void main(String[] args) throws Exception {
        long startTime;
        long connectionTimes[] = new long[10] ;
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty(PGProperty.USER.getName(), "postgres");
        connectionProperties.setProperty(PGProperty.PASSWORD.getName(), "password");
        connectionProperties.setProperty(PGProperty.APPLICATION_NAME.getName(), "My App");
        for (int i = 0; i < 10; i++) {
            startTime = System.currentTimeMillis();

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://" + host + "/postgres", connectionProperties)) {
                connectionTimes[i] = System.currentTimeMillis()-startTime;

                try (PreparedStatement s = conn.prepareStatement("show application_name")) {
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
        long aggregateTime = 0;
        for (int i = 0; i < 10;i++){
            System.out.println("Connection time " + i + "=" + connectionTimes[i]);
            aggregateTime += connectionTimes[i];
        }
        System.out.println("Average Connection Time: " + aggregateTime/10);
    }
}
