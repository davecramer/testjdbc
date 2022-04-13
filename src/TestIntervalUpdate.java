import java.sql.*;
import java.util.Calendar;

/**
 * Created by davec on 2015-10-01.
 */
public class TestIntervalUpdate
{
    public static void main(String []args) throws Exception
    {
        String url = "jdbc:postgresql://localhost/test";
        //url = "jdbc:pgsql://localhost:5432/test";

        try (Connection con = DriverManager.getConnection(url, "test", "test"))
        {
            con.createStatement().execute("drop table if EXISTS  t");
            con.createStatement().execute("CREATE TABLE t ( t timestamp)");

            try (PreparedStatement pstmt = con.prepareStatement( "insert into t values (? - interval '30 seconds')")){
                pstmt.setTimestamp(1, new Timestamp(Calendar.getInstance().getTimeInMillis()));
                pstmt.executeUpdate();
            }

            con.createStatement().execute("drop table t");
        }
    }
}
