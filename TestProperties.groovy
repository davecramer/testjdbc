import java.sql.DriverManager

public class PGTest {
        public static void main(String[] args) throws Exception {
                Class.forName("org.postgresql.Driver");
                Properties info = new Properties();
                info.put("foo", new Object());      // info.getPropert("foo") will return null
                DriverManager.getConnection("foo:bar//baz", info);
        }
}
