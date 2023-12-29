import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class TestAtomicProcedure {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        PGProperty.USER.set(props,"davec");
        PGProperty.PASSWORD.set(props, "password");
        //props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");
        try (Connection con = DriverManager.getConnection(url, props)) {

// Setup procedure to call.
            try (Statement stmt = con.createStatement()) {
                stmt.execute("CREATE TEMP TABLE temp_val ( some_val bigint )");
                stmt.execute("CREATE OR REPLACE FUNCTION asterisks(n int)\n" +
                        "    RETURNS SETOF text\n" +
                        "    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE\n" +
                        "    BEGIN ATOMIC\n" +
                        "    SELECT repeat('*', g) FROM generate_series (1, n) g; -- <-- Note this semicolon\n" +
                        "            END;");
            }
        } catch (Exception ex ){
            ex.printStackTrace();
        }
    }
}
