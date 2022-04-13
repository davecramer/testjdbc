import org.postgresql.PGProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.Properties;

public class TestAutoSave {
  public static void main(String []args){
    Properties connectionProperties =  new Properties();
    connectionProperties.setProperty(PGProperty.USER.getName(),"davec");
    connectionProperties.setProperty(PGProperty.PASSWORD.getName(),"password");
    connectionProperties.setProperty(PGProperty.AUTOSAVE.getName(), "conservative");

    try (Connection conn = DriverManager.getConnection(
        "jdbc:postgresql://localhost/test", connectionProperties)) {

      try (Statement stmt = conn.createStatement() ) {
        stmt.execute("create schema customer1");
        stmt.execute("create table customer1.test(i int4)");
        stmt.execute("create schema customer2");
        stmt.execute("create table customer2.test(i varchar)");

        conn.setAutoCommit(false);
        conn.setSchema("customer1");
        try (PreparedStatement pstmt = conn.prepareStatement("select * from test")) {
          for (int i = 0; i < 10; i++) {
            try (ResultSet rs = pstmt.executeQuery()) {
              rs.next();
            }
          }
          conn.setSchema("customer2");
          try (ResultSet rs = pstmt.executeQuery()) {
            rs.next();
          }

        }
        stmt.execute("drop schema customer1 CASCADE ");
        stmt.execute("drop schema customer2 CASCADE ");
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }

}
