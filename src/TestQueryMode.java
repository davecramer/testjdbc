import org.postgresql.PGProperty;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TestQueryMode {
  public static void main(String []args){
    String [] queryModes = {"extendedForPrepared", "extendedCacheEverything", "extended", "simple"};

    Properties properties = new Properties();
    properties.setProperty(PGProperty.USER.getName(),"davec");
    properties.setProperty(PGProperty.PASSWORD.getName(),"password");
    properties.setProperty(PGProperty.LOGGER_LEVEL.getName(), "TRACE");
    properties.setProperty(PGProperty.LOGGER_FILE.getName(), "/tmp/jdbc.log");
    DriverManager.setLogWriter(new PrintWriter(System.err));

    for (String queryMode:queryModes) {
      properties.setProperty(PGProperty.PREFER_QUERY_MODE.getName(), queryMode);

      System.err.println("\n\n\n  query mode " + queryMode);
      try (Connection
          connection = DriverManager.getConnection("jdbc:postgresql://localhost/test", properties)) {

        try (Statement statement = connection.createStatement()) {
          statement.execute("select 1");
        }
      }catch (SQLException ex ){
        ex.printStackTrace();
      }
    }
  }
}
