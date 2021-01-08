import java.sql.*;
import java.util.Properties;

/**
 * Test usage of Stored Proc's from JDBC
 */
public final class StoredProcTransTest  {

    private StoredProcTransTest() {}

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Connection conn = null;

        String url = "jdbc:postgresql://localhost/test11?loggerLevel=debug";
        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        props.setProperty("ssl", "false");

        conn = DriverManager.getConnection(url, props);

        // autoCommit = false generates PSQL exeception
        //Exception in thread "main" org.postgresql.util.PSQLException: ERROR: invalid transaction termination
        //  Where: PL/pgSQL function test_proctrans(integer,integer) line 4 at COMMIT
        boolean autoCommit = false;

        conn.setAutoCommit(autoCommit);

        System.out.println("Starting procedure testing");

        Statement st = conn.createStatement();
        st.executeUpdate("CREATE OR REPLACE PROCEDURE test_proctrans(IN input int, INOUT results int) \n"
                + "LANGUAGE 'plpgsql' \n"
                + "AS $BODY$ \n"
                + "BEGIN \n"
                + "    select input * 3 into results; \n"
                + "    commit; \n"
                + "    select input * 2 into results; \n"
                + "END; \n"
                + "$BODY$;");
        System.out.println("Created test_proctrans.");


        System.out.println("Calling test_proctrans w/ autocommit = " + autoCommit);
        if(!autoCommit)
            conn.commit();
        CallableStatement callableStatement = conn.prepareCall("CALL public.test_proctrans(?, ?)");
        callableStatement.setInt(1, 5);
        callableStatement.setInt(2, 0);
        boolean b = callableStatement.execute();
        if (b) {
            ResultSet rs = callableStatement.getResultSet();
            rs.next();
            System.out.print("Output parameter: ");
            System.out.println(rs.getInt(1));
            rs.close();
        }
        callableStatement.close();

        System.out.println("Tested transaction test_proctrans(conn, " + autoCommit + ")");

        st.executeUpdate("DROP PROCEDURE test_proctrans(int, int)");
        System.out.println("Dropped test_proctrans.");

        conn.close();
        System.out.println("Goodbye World!");

    }

}
