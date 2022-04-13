import org.postgresql.Driver
import org.postgresql.PGProperty

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.sql.ResultSet

/**
 * Created by davec on 2016-03-10.
 */
class TestFetchSize {
    public static void main(String[] args)
    {
        Properties properties = new Properties()
        properties.setProperty(PGProperty.USER.getName(),"davec")
        properties.setProperty(PGProperty.PASSWORD.getName(),"password")
        properties.setProperty(PGProperty.LOGGER_LEVEL.getName(), "TRACE")
        properties.setProperty(PGProperty.PREFER_QUERY_MODE.getName(),"extended")

        DriverManager.setLogWriter(new PrintWriter(System.err));

        Connection con = DriverManager.getConnection('jdbc:postgresql://localhost:5432/test',properties)

        con.setAutoCommit(true)

        Statement stmt = con.pr(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY)
        stmt.setFetchSize(10)
        ResultSet rs = stmt.executeQuery('select * from fetchrows')
        while (rs.next())
        {
            rs.getInt('id');
        }

    }

}
