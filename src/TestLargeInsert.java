import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestLargeInsert {
    public static void main(String []args) throws Exception {

        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "davec");
        props.setProperty("password", "");

        // result = s.length <= 65,839 ? success : failure
        String s = repeat("tibsy", 14000) + "theEnd";
        try(Connection conn = DriverManager.getConnection(url, props);
            Statement stat = conn.createStatement())
        {
            stat.executeUpdate("update sometable set textfield = '" + s + "'");
        }
    }
    static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder(s);
        while (n-- > 0 ) {
            sb.append(s);
        }
        return sb.toString();
    }
}

