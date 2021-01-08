import java.sql.*;
import java.util.Properties;

public class TestCreateDrop {

    public static String createTable = "CREATE TABLE IF NOT EXISTS foo.newtable" +
            "( col1 varchar," +
            "col2 varchar)";


    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/test";
        double[] d = new double[4];

        Properties connectionProps = new Properties();
        connectionProps.put("user", "test");
        connectionProps.put("password", "test");
        try ( Connection conn =DriverManager.getConnection(url,connectionProps)) {
                executeUpdateSql(conn, "create schema foo");
                executeUpdateSql(conn, createTable);
                executeQuery(conn, "select * from foo.newtable");
                executeUpdateSql(conn, "drop schema foo cascade");
        } catch (SQLException ex ) {
            ex.printStackTrace();
        }
    }
    public static void executeQuery(Connection con, String sql) {

        try (Statement sta = con.createStatement()) {
            try (ResultSet rs = sta.executeQuery(sql)) {
                while (rs.next()) {
                    rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    public static void executeUpdateSql(Connection con, String sql) {

        try (Statement sta = con.createStatement()) {

            int count = sta.executeUpdate(sql);
            if (count != 0) {
                System.err.println("SUCCESS EXECUTE SQL: " + sql);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}
