
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestUnicodeChar {
    public static void main(String []args) throws Exception {

        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try ( Connection conn = DriverManager.getConnection(url, props) ){
            try ( Statement statement = conn.createStatement() ) {
                try (ResultSet rs = statement.executeQuery( "select '❤'::bpchar") ){
                    if (rs.next())
                        System.out.println( "Get String: " + rs.getString(1));
                }
            }
        }
    }
}