import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

/**
 * Created by davec on 2017-05-17.
 */
/*
SELECT  count(*) FILTER (WHERE i < 5) AS filtered
FROM generate_series(1,10) AS s(i);
 */
public class TestFilter {
  public static void main(String[] args) throws Exception {
    try(Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "test", "");
        Statement stmt = c.createStatement()) {
      stmt.execute("SELECT  count(*) FILTER (WHERE i < 5) AS filtered\n"
          + "FROM generate_series(1,10) AS s(i);");
    }

  }
}
