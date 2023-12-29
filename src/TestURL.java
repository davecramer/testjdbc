import org.postgresql.PGConnection;
import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by davec on 2017-07-01.
 */
public class TestURL {
  public static void main(String []args) throws Exception
  {

    Properties props = new Properties();
    props.setProperty(PGProperty.PG_DBNAME.getName(),"test");
    props.setProperty(PGProperty.PG_HOST.getName(),"localhost");
    props.setProperty(PGProperty.PG_PORT.getName(),"5432");
    //props.setProperty(PGProperty.LOGGER_LEVEL.getName(), "DEBUG");

    props.setProperty("user","davec");
    props.setProperty("password", "");

    props.setProperty(PGProperty.PG_DBNAME.getName(),"postgres");
    props.setProperty(PGProperty.PG_HOST.getName(),"localhost");
    props.setProperty("user","davec");
    props.setProperty("password", "");



    Connection connection;
    /*= DriverManager.getConnection(
            "jdbc:postgresql://pgjdbc.postgres.database.azure.com:5432/postgres?user=postgres@pgjdbc&password=9!pD7Mx2b8");

    PGConnection pgConnection =  connection.unwrap(PGConnection.class);

    executeSql(connection, "select 1");

    connection.close();
     */
    connection = DriverManager.getConnection("jdbc:postgresql://localhost/aaa/bbb", "davec", "password");
    connection.close();
    connection = DriverManager.getConnection("jdbc:postgresql://localhost/","davec", "password");
    connection.close();
    connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/","davec", "password");
    connection.close();
    connection = DriverManager.getConnection("jdbc:postgresql:davec","davec", "password");
    connection.close();
    connection = DriverManager.getConnection("jdbc:postgresql://","davec", "password");
    connection.close();
    connection = DriverManager.getConnection("jdbc:postgresql:/","davec", "password");
    connection.close();


  }
  public static void executeSql(Connection con, String sql)
  {
    try(Statement stmt = con.createStatement()){
      stmt.execute(sql);
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
  }
}
