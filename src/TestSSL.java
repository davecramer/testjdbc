import org.postgresql.PGConnection;
import org.postgresql.PGProperty;

import java.sql.*;
import java.util.Properties;

public class TestSSL {
  public static void main(String []args) throws Exception
  {

    Properties props = new Properties();
    props.setProperty(PGProperty.PG_DBNAME.getName(),"postgres");
    props.setProperty(PGProperty.PG_HOST.getName(),"database-2.cluster-cgnh50a2ovor.us-east-1.rds.amazonaws.com");
    props.setProperty(PGProperty.PG_PORT.getName(),"5432");
    props.setProperty(PGProperty.SSL.getName(),"true");
    props.setProperty(PGProperty.SSL_MODE.getName(),"verify-full");

    props.setProperty("user","postgres");
    props.setProperty("password", "password");
    props.setProperty(PGProperty.SSL_ROOT_CERT.getName(), "classpath://global-bundle.pem");
    //props.setProperty(PGProperty.SSL_KEY.getName(), "/Users/davec/projects/mlstest/certs/secret-client-key.pem");
    //props.setProperty(PGProperty.SSL_ROOT_CERT.getName(), "/Users/davec/projects/mlstest/certs/rootCA.pem");


    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://", props)){
      try (Statement statement = connection.createStatement()){
        try (ResultSet rs = statement.executeQuery("SELECT * from pg_stat_ssl where pid = pg_backend_pid()")){
          if (rs.next()){
            boolean b = rs.getBoolean(2);
            System.out.println("SSl is: "+ b);
          }
        }
      }
    }catch ( Exception ex ){
      ex.printStackTrace();
    }

  }

}
