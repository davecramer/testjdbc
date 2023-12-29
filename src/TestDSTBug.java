import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Properties;

public class TestDSTBug {
    public static void main(String []args) throws Exception {

        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        LocalDateTime ts = LocalDateTime.of(2023,3,12,2,0); //DST boundary in Eastern Time Zone
        try ( Connection conn = DriverManager.getConnection(url, props) ){
            try ( PreparedStatement statement = conn.prepareStatement("select * from (values(?::timestamp)) as v(ts)")) {
                statement.setObject(1, ts);
                try (ResultSet rs = statement.executeQuery( )){
                    if (rs.next())
                        System.out.println("before, after (jdbc): " + ts + ", " + rs.getObject(1, LocalDateTime.class));
                }
            }
        }
    }
}