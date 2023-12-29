import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.ResultSetMetaData;
        import java.sql.SQLException;
        import java.sql.Timestamp;

public class TestTimestampInterval {

    public static void main(String[] args) throws SQLException {

        String url = "jdbc:postgresql://localhost/test";

        Connection conn = DriverManager.getConnection(url, "davecra", "test");

        System.out.println("Executing query 00 ...");
        try (PreparedStatement ps02 = conn.prepareStatement(" select ? as tt ");) {
            ps02.setTimestamp(1, new Timestamp(System.currentTimeMillis()), null);
            ResultSet rs = ps02.executeQuery();

            ResultSetMetaData metadata = rs.getMetaData();
            System.out.println("column type : " + metadata.getColumnType(1));
            System.out.println("varchar ? :" + java.sql.Types.VARCHAR);
            while (rs.next()) {
                System.out.println("tt:" + rs.getString(1));
                System.out.println("tt:" + rs.getTimestamp(1));
            }
        }

        conn.prepareStatement(" drop table if exists test ").executeUpdate();
        conn.prepareStatement(" create table if not exists test(id bigint, pname text, create_dt timestamptz) ")
                .executeUpdate();

        System.out.println("insert some data..");
        conn.prepareStatement(" insert into test values( 1, 'hello', now() - interval '1 day' ) ").executeUpdate();
        conn.prepareStatement(" insert into test values( 2, 'world', now() - interval '1 day' ) ").executeUpdate();

        System.out.println("Executing query 01 ...");
        try (PreparedStatement ps01 = conn.prepareStatement("select * from  test  where create_dt < ? ");) {
            ps01.setTimestamp(1, new Timestamp(System.currentTimeMillis()), null);
            ResultSet rs = ps01.executeQuery();
            while (rs.next()) {
                System.out.println("query 01 id:" + rs.getInt("id"));
            }
        }

        System.out.println("Executing query 02 ...");
        try (PreparedStatement ps02 = conn
                .prepareStatement("select * from  test  where create_dt < ?  + interval '1 day' ");) {
            ps02.setTimestamp(1, new Timestamp(System.currentTimeMillis()), null);
            ResultSet rs = ps02.executeQuery();
            while (rs.next()) {
                System.out.println("query 02 id:" + rs.getInt("id"));
            }
        }


    }
}
