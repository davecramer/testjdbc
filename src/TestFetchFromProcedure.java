import java.sql.*;

public class TestFetchFromProcedure {

    public static void setup(Connection con) throws SQLException {
        try (Statement statement = con.createStatement()) {
            statement.execute("create table if not exists  test_blob(content bytea)");
            statement.execute("--create function to read data\n" +
                    "            CREATE OR REPLACE FUNCTION test_blob(p_cur OUT REFCURSOR) AS $body$\n" +
                    "            BEGIN\n" +
                    "            OPEN p_cur FOR SELECT content FROM test_blob;\n" +
                    "            END;\n" +
                    "            $body$ LANGUAGE plpgsql STABLE");

            statement.execute("--generate 101 rows with 4096 bytes:\n" +
                    "            insert into test_blob\n" +
                    "            select(select decode(string_agg(lpad(to_hex(width_bucket(random(), 0, 1, 256) - 1), 2, '0'), ''), 'hex')FROM generate_series(1, 4096))\n" +
                    "            from generate_series (1, 101)");


        }
    }
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost/postgres?defaultRowFetchSize=50";
        Connection conn = DriverManager.getConnection(url, "test", "test");

        setup(conn);
        conn.setAutoCommit(false);

        int cnt = 0;

        try (CallableStatement stmt = conn.prepareCall("{? = call test_blob()}")) {
            stmt.registerOutParameter(1, Types.REF_CURSOR);
            // stmt.setFetchSize(0); // has no effect
            stmt.execute();
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                // rs.setFetchSize(0); // has no effect
                while (rs.next()) {
                    cnt++;
                    if (cnt == 50) {
                        System.err.println("got here");
                    }
                }
            }
        } catch (SQLException ex ) {
            ex.printStackTrace();
        } finally {
            System.out.println("records read: " + cnt);
        }
        conn.close();
    }

}


