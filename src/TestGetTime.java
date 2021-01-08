import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.TimeZone;

public class TestGetTime {
    public static void main(String []args) throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try ( Connection conn = DriverManager.getConnection(url, props) ){
            try ( Statement statement = conn.createStatement() ) {
                try (ResultSet rs = statement.executeQuery( "select current_time") ){
                    rs.next();
                    System.out.println( "Get Time: " + rs.getTime(1));
                    System.out.println( "Get String: " + rs.getString(1));
                }
            }
        }
    }
}
