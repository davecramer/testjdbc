import org.postgresql.PGProperty;

import javax.security.auth.login.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/*
set the environment variables to

KRB5CCNAME=/usr/local/src/postgresql/src/test/kerberos/tmp_check/krb5cc;KRB5_CONFIG=/usr/local/src/postgresql/src/test/kerberos/tmp_check/krb5.conf;KRB5_KDC_PROFILE=/usr/local/src/postgresql/src/test/kerberos/tmp_check/kdc.conf
KRB5CCNAME=/Users/davec/projects/jdbc/pgjdbc.git/test-gss/tmp_check/krb5cc;KRB5_CONFIG=/Users/davec/projects/jdbc/pgjdbc.git/test-gss/tmp_check/krb5.conf;KRB5_KDC_PROFILE=/Users/davec/projects/jdbc/pgjdbc.git/test-gss/tmp_check/kdc.conf
 */
public class TestKerberos {
    public static void main(String[] args) {

        String prefix = "/Users/davec/projects/jdbc/pgjdbc.git/test-gss/"; //
        //prefix = "/usr/local/src/postgresql/src/test/kerberos/";
        String krb5Conf = prefix + "tmp_check/krb5.conf";
        System.setProperty("sun.security.jgss.native", "true");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        Configuration configuration = Configuration.getConfiguration();
        Properties properties = new Properties();
        PGProperty.USER.set(properties,"test1");
        PGProperty.PASSWORD.set(properties, "secret");
        PGProperty.GSS_ENC_MODE.set(properties,"require");
        PGProperty.JAAS_LOGIN.set(properties, true);
        PGProperty.JAAS_APPLICATION_NAME.set(properties, "pgjdbc");
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://auth-test-localhost.postgresql.example.com:57828/postgres", properties)){
            try (Statement statement = con.createStatement()) {
                try ( ResultSet rs = statement.executeQuery( "SELECT gss_authenticated AND encrypted from pg_stat_gssapi where pid = pg_backend_pid()")) {
                    if ( rs.next() ) {
                        System.err.println("GSS auth and encrypted: " + rs.getBoolean(1));
                    } else {
                        System.err.println("Error, should have received one row");
                    }
                }
            }
        }catch ( Exception ex ) {
            System.err.println(ex.getMessage());
        }
    }
}
