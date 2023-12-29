
import java.sql.*;
import java.util.Properties;

public class TestCastCallable {
    public static void main(String []args) throws Exception {


        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try ( Connection conn = DriverManager.getConnection(url, props) ){
            try (CallableStatement stmt =
                         conn.prepareCall("call test_proc(?)")) {
                stmt.setNull(1, java.sql.Types.BIGINT);
                stmt.registerOutParameter(1, java.sql.Types.BIGINT);
                stmt.execute();
                Object object = stmt.getObject(1, Long.class);
                System.out.println(object);
            }
        }
    }
}