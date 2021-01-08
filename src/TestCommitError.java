import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestCommitError {
    public static void main(String []args) throws SQLException  {
        String url = "jdbc:postgresql://localhost:5432/test";
        Connection conn = null;

        Properties connectionProps = new Properties();
        connectionProps.put("user", "test");
        connectionProps.put("password", "test");
        conn = DriverManager.getConnection(url, connectionProps);
        try {
            conn.setAutoCommit(false);
            try {
                conn.createStatement().execute("insert into tt values (NULL)");
            } catch (SQLException ex ) {
                ex.printStackTrace();
                //ignore this exception
            }
            conn.commit();
        } catch ( SQLException ex ) {
            ex.printStackTrace();
            conn.rollback();
        }
        conn.close();
    }
}
