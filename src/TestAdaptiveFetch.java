import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;
import java.util.logging.LogManager;

public class TestAdaptiveFetch {


    /*
    create table growing_rows(id serial, txt text);
    insert into growing_rows (txt) values
        ((select string_agg(md5(random()::text), ' ') from generate_series(1, 1))),
        ((select string_agg(md5(random()::text), ' ') from generate_series(1, 1000))),
        ((select string_agg(md5(random()::text), ' ') from generate_series(1, 2000)));
Enable adaptiveFetch
    props.setProperty("adaptiveFetch", "true");
    props.setProperty("defaultRowFetchSize", "1");
    props.setProperty("maxResultBuffer", "90K");
    Connection conn = DriverManager.getConnection(url, props);
    conn.setAutoCommit(false);
Execute statement select * from growing_rows order by id

     */
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    public static void main(String[] args) throws Exception {
        LogManager.getLogManager().readConfiguration(TestAdaptiveFetch.class.getResourceAsStream("/logging.properties"));
        /*
        try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", USER, PASSWORD)){
            createTable(c);
            insertRows(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */

        Properties properties = new Properties();
        PGProperty.ADAPTIVE_FETCH.set(properties, true);
        PGProperty.DEFAULT_ROW_FETCH_SIZE.set(properties, 1);
        PGProperty.MAX_RESULT_BUFFER.set(properties,"100k");
        PGProperty.USER.set(properties, "test");
        PGProperty.PASSWORD.set(properties, "test");

        try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",properties)) {
            c.setAutoCommit(false);

            try (PreparedStatement p = c.prepareStatement("select * from growing_rows order by id")) {
                p.setFetchSize(1);
                try (ResultSet rs = p.executeQuery()) {
                    while(rs.next());
                }
            }
            c.commit();
        }
    }
    public static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()){
            statement.execute("create table if not exists growing_rows(id serial, txt text)");
        }
    }
    public static void insertRows(Connection connection) throws SQLException {
        try( Statement statement = connection.createStatement()) {
            statement.execute("insert into growing_rows (txt) values ((select string_agg(md5(random()::text), ' ') from generate_series(1, 1)))");
            statement.execute("insert into growing_rows (txt) values ((select string_agg(md5(random()::text), ' ') from generate_series(1, 1000)))");
            statement.execute("insert into growing_rows (txt) values ((select string_agg(md5(random()::text), ' ') from generate_series(1, 2000)))");
        }
    }

}
