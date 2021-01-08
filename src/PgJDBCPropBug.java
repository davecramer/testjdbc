/**
 * Created by davec on 2016-01-11.
 */
import java.util.Properties;

import java.sql.*;

public class PgJDBCPropBug
{
    public static void sslAsProp (final String url,
                                  final String uid,
                                  final String pwd)
    {
        Properties props = new Properties ();
        Connection conn = null;

        props.put ("user", uid);
        props.put ("password", pwd);
        props.put ("ssl", "true");

        try {
            System.out.println ("Trying to connect to : " + url);
            conn = DriverManager.getConnection (url, props);
            System.out.println ("Succeeded to create connection in sslAsProp");
        } catch (SQLException x) {
            System.out.println ("Failed to create connection in sslAsProp");
            x.printStackTrace ();
        }
    }

    public static void sslAsUrl (final String base_url,
                                 final String uid,
                                 final String pwd)
    {
        Properties props = new Properties ();
        Connection conn = null;

        // we only put ssl in the url, credentials are still with properties
        props.put ("user", uid);
        props.put ("password", pwd);

        try {
            final String url = base_url + "?ssl=true";

            System.out.println ("Trying to connect to : " + url);
            conn = DriverManager.getConnection (url, props);
            System.out.println ("Succeeded to create connection in sslAsUrl");
        } catch (SQLException x) {
            System.out.println ("Failed to create connection in sslAsUrl");
            x.printStackTrace ();
        }
    }

    public static void main (final String[] args)
    {
        final String base_url = args[0];
        final String user = args[1];
        final String password = args[2];

        sslAsProp (base_url, user, password); // fails in 1207, but works in < 1207
        sslAsUrl (base_url, user, password); // works in all versions
    }
}
