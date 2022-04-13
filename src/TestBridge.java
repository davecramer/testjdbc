import java.sql.*;

public class TestBridge {
    public static void main(String[] args) {
        try(Connection con = DriverManager.getConnection(
                "jdbc:postgresql://p.thatmhx5vndt5c6wijo7s6w45m.db.postgresbridge.com/postgres?user=postgres&password=RMu8TvaZw9Shj0s6sFQDLkMK5A9GxSQ501iK8vW8bln8pgGz2UoQJ2Yd0Nvu6Oxo")) {
            try (Statement stmt = con.createStatement()) {
                try ( ResultSet rs = stmt.executeQuery("select 1")){
                    rs.next();
                }

            }
        } catch ( SQLException ex ){
            ex.printStackTrace();
        }


    }
}
