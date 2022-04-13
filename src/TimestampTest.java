
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class TimestampTest {

	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
	static final String USER = "test";
	static final String PASS = "password";

	public static void main (String[] args) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;

		try {
			// initial connection with binary transfer ON
			Class.forName(JDBC_DRIVER);
			System.setProperty("org.postgresql.forceBinary", "true");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();

			// test data
			try {
                stmt.execute("SET SESSION TIME ZONE 'America/Chicago'");
				stmt.execute("CREATE TABLE tstable (tsfield TIMESTAMP(0) WITHOUT TIME ZONE)");
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt.execute("truncate table tstable");
			stmt.execute("insert into tstable (tsfield) values ('2014-10-26 01:00:04')");

			// **********************************************************************
			// TEST WITH TIMESTAMP BINARY TRANSFER ON
			rs = stmt.executeQuery("SELECT tsfield FROM tstable");
			rs.next();
			Timestamp ts = rs.getTimestamp("tsfield");
			System.out.println(ts);
			rs.close();
			stmt.close();
			conn.close();

			// **********************************************************************
			// TEST WITH TIMESTAMP BINARY TRANSFER OFF
			conn = DriverManager.getConnection(DB_URL + "?binaryTransferDisable=TIMESTAMP", USER,
					PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT tsfield FROM tstable");
			rs.next();
			ts = rs.getTimestamp("tsfield");
			System.out.println(ts);
			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
				// nop;
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception se) {
				// nop;
			}
		}
	}
}
