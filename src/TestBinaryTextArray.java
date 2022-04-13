/**
 * Created by davec on 2015-02-03.
 */





import java.sql.*;
import java.util.Properties;

public class TestBinaryTextArray
{
    public static void main(String[] args) throws SQLException
    {
        String url = "jdbc:postgresql://localhost/test";
        Connection conn = null;
        // org.postgresql.Driver.setLogLevel(org.postgresql.Driver.DEBUG);

        Properties connectionProps = new Properties();
        connectionProps.put("user", "test");
        connectionProps.put("password", "password");
        connectionProps.put("binaryTransfer", "true");
        conn = DriverManager.getConnection(url, connectionProps);

        Driver driver = DriverManager.getDriver(url);
        conn = driver.connect(url, connectionProps);

        PreparedStatement fs = conn
                .prepareStatement("SELECT proargnames FROM pg_proc where proname ='pg_cursor'");

        ((org.postgresql.PGStatement) fs).setPrepareThreshold(1);
        // execute twice to use prepared statement 2nd time
        for (int i = 0; i < 2; i++) {
            ResultSet rs = fs.executeQuery();
            rs.next();

            Array array = rs.getArray(1);
            System.out.println(array);
            rs.close();
        }
    }
}
