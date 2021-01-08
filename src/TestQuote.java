
import java.io.PrintWriter;
import java.sql.*;
import java.sql.Driver;
import java.util.Properties;

/**
 * Created by davec on 2014-04-18.
 */
public class TestQuote
{
    public static final String DBURL = "jdbc:postgresql:test";
    public static final String DBUSER = "postgres";


    public static Connection createConnection () throws SQLException {
        Properties props = new Properties();
        props.setProperty("user",DBUSER);
        return DriverManager.getConnection(DBURL, props);
    }

    public static void main(String []args)
    {
        try {

            DriverManager.setLogWriter(new PrintWriter(System.err));
            //org.postgresql.Driver.setLogLevel(org.postgresql.Driver.DEBUG);

            Connection c = createConnection();

            PreparedStatement s = c.prepareStatement
                    ("delete from black_list where value = ?");

            s.setString( 1, "load the \"" );

            int i = s.executeUpdate();

            System.out.println("deleted " + i + " rows");
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.exit( 2 );
        }

    }
}
