import org.postgresql.PGProperty;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.LogManager;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by davec on 2015-09-28.
 */
public class TestDate
{
    public static void main2(String []args) throws Exception
    {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        String url = //"jdbc:postgresql://localhost/test?socketFactory=org.newsclub.net.unix.socketfactory.PostgresqlAFUNIXSocketFactory&socketFactoryArg=/tmp/.s.PGSQL.5432";
        url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        props.setProperty("prepareThreshold", "-1");
        props.getProperty(PGProperty.OPTIONS.getName(),"-c timezone=America/New_York");
        Connection conn = DriverManager.getConnection(url, props);

        Statement stmt1 = conn.createStatement();
        ResultSet rstz = stmt1.executeQuery("SELECT CURRENT_SETTING('TIMEZONE') as tz, LOCALTIMESTAMP lts, CURRENT_TIMESTAMP cts, LOCALTIME lt, " +
                "CURRENT_TIME ct, CAST(CURRENT_TIME AS VARCHAR(200)) ct_varchar");
        rstz.next();
        System.out.println( "TZ: " + rstz.getString("tz") + " lts: " + rstz.getString("lts")
        + " cts: " + rstz.getString("cts") + " lt: " + rstz.getString("lt")
        + " ct: " + rstz.getString("ct") + " ct str: " + rstz.getString("ct_varchar"));
        rstz.close();
        stmt1.close();



        Statement stmt = conn.createStatement();

        ResultSet rs1 = stmt.executeQuery("show timezone");
        rs1.next();
        System.out.println("Timezone: " + rs1.getString(1) );

        final String tableTestName = "_jdbctest1";

        //@formatter:off
        stmt.execute("CREATE TABLE " + tableTestName
                + "  (id serial primary key,"
                + "  datecol timestamp without time zone)"
                );
        //@formatter:on


        stmt.execute("insert into " + tableTestName
                + " (datecol) values ('infinity')");

        stmt.execute("insert into " + tableTestName
                + " (datecol) values ('-infinity')");


        PreparedStatement pstmt =
                conn.prepareStatement("SELECT datecol FROM " + tableTestName + " order by id");
        Timestamp ts;
        Date date;

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                System.out.println("as String:\t" + rs.getString("datecol"));

                ts = rs.getTimestamp("datecol");
                System.out.println("as Timestamp:\t" + ts);

                date = rs.getDate(("datecol"));
                System.out.println("as Date:\t" + date);
                System.out.println("---");
            }


        stmt.execute("drop table " + tableTestName);
    }

    public static void main(String[] args) {
       /* InputStream stream = TestDate.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        //props.setProperty("prepareThreshold", "-1");

        try ( Connection con = DriverManager.getConnection(url, props)) {
            Statement stmt = con.createStatement();
            stmt.execute("create table if not exists testtz (dt timestamptz)");
            stmt.execute("truncate testtz");
            assertEquals(1, stmt.executeUpdate("insert into testtz values ('0101-01-01 BC')" ));
            assertEquals(1, stmt.executeUpdate("insert into testtz values ('0001-01-01')" ));
            assertEquals(1, stmt.executeUpdate("insert into testtz values ('0001-01-01 BC')" ));
            assertEquals(1, stmt.executeUpdate("insert into testtz values ('0001-12-31 BC')" ));

            try ( ResultSet rs = stmt.executeQuery("select dt from testtz")) {
                Date d = null;
                assertTrue(rs.next());
                d = rs.getDate(1);
                assertNotNull(d);
                //0101-01-01 BC
                assertEquals(makeDate(-100, 1, 1), d);
                assertTrue(rs.next());
                d = rs.getDate(1);
                assertNotNull(d);
                // 0001-01-01
                assertEquals(makeDate(1, 1, 1), d);

                assertTrue(rs.next());
                d = rs.getDate(1);
                assertNotNull(d);
                // 0001-01-01 BC
                assertEquals(makeDate(0, 1, 1), d);

                assertTrue(rs.next());
                d = rs.getDate(1);
                assertNotNull(d);
                // 0001-12-31 BC
                assertEquals(makeDate(0, 12, 31), d);

                assertTrue(!rs.next());
            }

        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    private static java.sql.Date makeDate(int y, int m, int d) {
        return new java.sql.Date(y - 1900, m - 1, d);
    }
}
