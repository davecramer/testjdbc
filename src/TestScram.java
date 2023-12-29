import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TestScram {
  public static void main(String[] args) throws SQLException {
    Properties properties = new Properties();
    properties.setProperty("loggerLevel", "TRACE");
    try (Connection connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost/postgres?user=scram&password=password",
            properties
    )) {

      try (Statement statement = connection.createStatement()){

        final ResultSet resultSet = statement.executeQuery("select version()");
        resultSet.next();
        System.out.println(resultSet.getString(1));
      }
    }
  }
}
