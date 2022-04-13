import java.sql.*;
import java.util.Properties;

public class TestSetTimeZone {
    public static void main(String []args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/test";
        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        props.setProperty("options","-c statement_timeout=90000 -c timezone=America/Toronto");
        try ( Connection conn = DriverManager.getConnection(url, props) ){
            try ( PreparedStatement statement = conn.prepareStatement("show all") ) {
                try (ResultSet rs = statement.executeQuery() ){
                    while (rs.next()) {
                        System.out.println(rs.getString(1)+"="+rs.getString(2));
                    }
                }
            }
        }
    }
}