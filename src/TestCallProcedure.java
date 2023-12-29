import org.postgresql.PGProperty;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class TestCallProcedure {
    public static void main(String[] args) {
        // set up a connection
        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        PGProperty.USER.set(props,"davec");
        PGProperty.PASSWORD.set(props, "password");
        // Ensure EscapeSyntaxCallmode property set to support procedures if no return value
        props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");
        try (Connection con = DriverManager.getConnection(url, props)) {

// Setup procedure to call.
            try (Statement stmt = con.createStatement()) {
                stmt.execute("CREATE TEMP TABLE temp_val ( some_val bigint )");
                stmt.execute("CREATE OR REPLACE PROCEDURE commitproc(a INOUT bigint) AS '"
                        + " BEGIN "
                        + "    INSERT INTO temp_val values(a); "
                        + "    COMMIT; "
                        + " END;' LANGUAGE plpgsql");
            }

// As of v11, we must be outside a transaction for procedures with transactions to work.
            con.setAutoCommit(true);

// Procedure call with transaction
            try (CallableStatement proc = con.prepareCall("{call commitproc( ? )}")) {
                proc.setInt(1, 100);
                proc.execute();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
