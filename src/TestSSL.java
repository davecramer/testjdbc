import org.postgresql.PGConnection;
import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TestSSL {
  public static void main(String []args) throws Exception
  {

    Properties props = new Properties();
    props.setProperty(PGProperty.PG_DBNAME.getName(),"postgres");
    props.setProperty(PGProperty.PG_HOST.getName(),"secret.selinux-el8-primary.com");
    props.setProperty(PGProperty.PG_PORT.getName(),"5432");
    props.setProperty(PGProperty.SSL.getName(),"true");
    props.setProperty(PGProperty.SSL_MODE.getName(),"verify-full");

    props.setProperty("user","dbclient");
    props.setProperty("password", "H210kYHQbZJzw7");
    props.setProperty(PGProperty.SSL_CERT.getName(), "/Users/davec/projects/mlstest/certs/secret-client.p12");
    props.setProperty(PGProperty.SSL_KEY.getName(), "/Users/davec/projects/mlstest/certs/secret-client-key.pem");
    props.setProperty(PGProperty.SSL_ROOT_CERT.getName(), "/Users/davec/projects/mlstest/certs/rootCA.pem");

    /*
    props.setProperty("user","test");
    props.setProperty("password", "test");
    props.setProperty(PGProperty.SSL_CERT.getName(), "/Users/davec/.postgresql/goodclient.crt");
    props.setProperty(PGProperty.SSL_KEY.getName(), "/Users/davec/.postgresql/goodclient.key");
    props.setProperty(PGProperty.SSL_ROOT_CERT.getName(), "/Users/davec/.postgresql/goodroot.crt");
     */
    Connection connection = DriverManager.getConnection("jdbc:postgresql:",props);

    PGConnection pgConnection =  connection.unwrap(PGConnection.class);

    executeSql(connection, "select 1");

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
