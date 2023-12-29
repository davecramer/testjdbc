import java.io.IOException;
import java.sql.*;

public class CursorLeakTest {
    public static void main(String[] args) throws SQLException, IOException {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test",
                "test",
                "test")) {

            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("set standard_conforming_strings = on");
            }
            printCursors(conn);
        }
    }

    private static void printCursors(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.setFetchSize(0); // NOT if we set fetch size to 0, then select as expected will print single record
            try (ResultSet rs = stmt.executeQuery(" select * from pg_cursors")) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String sql = rs.getString("statement");
                    System.out.println(String.format("name='%s' sql='%s'", name, sql));
                }
            }
        }
    }
}
