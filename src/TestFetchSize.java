import org.postgresql.PGProperty;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by davec on 2016-03-10.
 */
class TestFetchSize {
    public static void main(String[] args)
    {
        Properties properties = new Properties();
        properties.setProperty(PGProperty.USER.getName(),"test");
        properties.setProperty(PGProperty.PASSWORD.getName(),"password");
        properties.setProperty(PGProperty.LOGGER_LEVEL.getName(), "TRACE");
        properties.setProperty(PGProperty.PREFER_QUERY_MODE.getName(),"extended");
        //PGProperty.ADAPTIVE_FETCH.set(properties, true);
        //PGProperty.DEFAULT_ROW_FETCH_SIZE.set(properties,64);

        DriverManager.setLogWriter(new PrintWriter(System.err));

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test?adaptiveFetch=true&defaultRowFetchSize=64",properties)) {

            con.setAutoCommit(false);

            Statement stmt = con.createStatement(); //ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY)
            ResultSet rs = stmt.executeQuery("select * from fetchrows");
            while (rs.next()) {
                rs.getInt("id");
            }
            con.commit();
        } catch (Exception ex ){
            ex.printStackTrace();
        }

    }

}
