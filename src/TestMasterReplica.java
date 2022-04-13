import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TestMasterReplica {

  static String [] targetServerTypes = {"master","secondary","preferSecondary"};

  public static void main(String []args){

    Properties connectionProperties =  new Properties();
    connectionProperties.setProperty(PGProperty.USER.getName(),"davec");
    connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"password");

    for ( String targetServerType:targetServerTypes ) {
      System.out.println( "Target Server type: " + targetServerType);
      try (Connection conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5434,localhost:5433/test?targetServerType="+targetServerType, connectionProperties)) {
        System.out.println(" connection is " + (isSecondary(conn)?"secondary":"master"));

      } catch (SQLException ex) {
        System.out.println(ex.getMessage());
      }
    }

    for ( String targetServerType:targetServerTypes ) {
      System.out.println( "Target Server type: " + targetServerType);
      try (Connection conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost,localhost:5433/test?targetServerType="+targetServerType, connectionProperties)) {
        System.out.println(" connection is " + (isSecondary(conn)?"secondary":"master"));

      } catch (SQLException ex) {
        System.out.println(ex.getMessage());
      }
    }

  }

  public static boolean isSecondary(Connection con){
    try (Statement statement = con.createStatement()){
      try (ResultSet rs = statement.executeQuery("select pg_is_in_recovery()" )){
        if (rs.next()){
          return rs.getBoolean(1);
        }
      }
    } catch (SQLException ex){
      ex.printStackTrace();
    }
    return false;
  }

}
