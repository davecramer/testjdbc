import java.sql.*;
/**
 * Created by davec on 2017-05-15.
 */
public class TestBatch {

  public static void main(String args[]) throws Exception {
    Class.forName("org.postgresql.Driver");


    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test?loggerLevel=TRACE&loggerFile=debug.log", "test", "");

    Statement stmt = conn.createStatement();
    stmt.execute("CREATE TEMP TABLE tmptab (id bigserial, val text)");
    stmt.close();

    for(int q=0; q<10; q++) {
      PreparedStatement ps = conn.prepareStatement("INSERT INTO tmptab (val) VALUES (?)");
      ParameterMetaData pmd = ps.getParameterMetaData();

      ps.setString(1, "a");
      ps.execute();
      ResultSet rs = ps.getGeneratedKeys();
      ResultSetMetaData rsmd = rs.getMetaData();
      while (rs.next()) {
        for (int j=1; j<=rsmd.getColumnCount(); j++) {
          System.out.print(rs.getString(j));
          System.out.print(", ");
        }
        System.out.println();
      }
      rs.close();
      ps.close();

    }
    {
      PreparedStatement ps = conn.prepareStatement("INSERT INTO tmptab (val) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, "a");
      ps.addBatch();
      ps.executeBatch();
      ResultSet rs = ps.getGeneratedKeys();
      ResultSetMetaData rsmd = rs.getMetaData();
      while (rs.next()) {
        for (int j=1; j<=rsmd.getColumnCount(); j++) {
          System.out.print(rs.getString(j));
          System.out.print(", ");
        }
        System.out.println();
      }
      rs.close();
      ps.close();
    }
    conn.close();
  }





}
