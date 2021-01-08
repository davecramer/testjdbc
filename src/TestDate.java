import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Created by davec on 2015-09-28.
 */
public class TestDate
{
    public static void main(String []args) throws Exception
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
}
