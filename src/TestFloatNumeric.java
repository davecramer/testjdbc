import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;

public class TestFloatNumeric {
    public static void main(String[] args)  throws SQLException  {
        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try ( Connection conn = DriverManager.getConnection(url, props) ){

            double d = -12345678.12345678d;
            System.out.println("double: " + d);
            //conn.createStatement().execute("create table pgsql_numeric_test(n8dp numeric(22,8), n8dp_2 numeric(22,8), n10dp numeric(22,10))");

            PreparedStatement ps = conn.prepareStatement("insert into pgsql_numeric_test(n8dp, n8dp_2, n10dp) VALUES (?,-1.234567812345678E7, ?)");
            ps.setBigDecimal(1, new BigDecimal(d));
            ps.setBigDecimal(2, new BigDecimal(d));
            ps.execute();
            ps.close();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select '<' || n8dp || '>', '<' || n8dp_2 || '>', '<' || n10dp || '>' from pgsql_numeric_test");
            rs.next();
            System.out.println("column 1: " + rs.getObject(1));
            System.out.println("column 2: " + rs.getObject(2));
            System.out.println("column 3: " + rs.getObject(3));

        }
    }
}
