import java.sql.*;

public class BinBatchPS {

	public static void main(String args[]) throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test?loglevel=2", "test", "");

		Statement stmt = conn.createStatement();
		stmt.execute("CREATE TEMP TABLE tmptab (id bigserial, val text)");
		stmt.close();

		PreparedStatement ps = conn.prepareStatement("INSERT INTO tmptab (val) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
		ParameterMetaData pmd = ps.getParameterMetaData();
		for (int i=0; i<5; i++) {
			ps.setString(1, "a");
			ps.addBatch();
		}
		ps.executeBatch();
		ResultSet rs = ps.getGeneratedKeys();
		ResultSetMetaData rsmd = rs.getMetaData();
		while (rs.next()) {
			for (int j=1; j<=rsmd.getColumnCount(); j++) {
				System.out.println(rs.getString(j));
			}
		}
		rs.close();
		ps.close();

		conn.close();
	}
}
