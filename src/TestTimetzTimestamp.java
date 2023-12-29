import java.sql.*;

public class TestTimetzTimestamp {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
    static final String USER = "test";
    static final String PASS = "password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            try ( Statement statement = connection.createStatement() ){
                try (ResultSet rs = statement.executeQuery("select t from tz") ){
                    if (rs.next()) {
                        Timestamp ts = rs.getTimestamp(1);
                        System.out.println(ts.toString());
                    }
                }
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
