/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-07-29
 * Time: 6:16 AM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.Test

import javax.sql.DataSource

//import org.postgresql.jdbc4.Jdbc4PoolingDataSource

import java.sql.*;
class TestUID  {
  Connection dbc;
  //Jdbc4PoolingDataSource dataSource = new Jdbc4PoolingDataSource();
  DataSource dataSource = null
  public init()
  {
  dataSource.setDataSourceName("test Pool");
  dataSource.setServerName('localhost');
  dataSource.setPortNumber(5432);
  dataSource.setDatabaseName('test');
  dataSource.setUser('test');
  dataSource.setPassword('');
  dataSource.setMaxConnections(40);
  dataSource.setLogWriter(new PrintWriter(System.out));
  }


  public static void main(String []args)
  {
    def testUID = new TestUID()
    testUID.init()
    testUID.testOtherSQLError()
    testUID.testOtherSQLError()

  }
  public void testOtherSQLError() throws Exception {
    final String uuid = "927b8a04-c319-4139-b985-79b1cbc43871";
    ResultSet result;
    PreparedStatement stmt;
    for (int i = 0; i < 10; i++) {
      dbc = dataSource.getConnection();
      try {
        stmt = dbc.prepareStatement("select * from table_with_uuid where no_such_col = ?");
        stmt.setString(1, "zzzzzzzzz");
        result = stmt.executeQuery();
        if (result.next()) {
          String s = result.getString(1);
          System.out.println("result: " + s);
        } else {
          System.out.println("no result");
        }
        result.close();
        stmt.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
      dbc.close();
    }

    System.out.println("----------------------------------------------------");
    dbc = dataSource.getConnection();
    stmt = dbc.prepareStatement("select * from table_with_uuid where uuid_col = ?");
    stmt.setString(1, uuid);
    result = stmt.executeQuery();
    if (result.next()) {
      String s = result.getString(1);
      System.out.println("result: " + s);
    } else {
      System.out.println("no result");
    }
    result.close();
    stmt.close();
    dbc.close();
  }

  @Test
  public void testUUIDSQLError() throws Exception {
    System.out.println("***************************** ^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    ResultSet result;
    PreparedStatement stmt;
    for (int i = 0; i < 5; i++) {
      dbc = dataSource.getConnection();
      try {
        String uuid = "92f0edb5-d155-4cf7-8af3-ef3194393ca5";
        stmt = dbc.prepareStatement("select uuid_col from table_with_uuid where uuid_col = ?");
        stmt.setString(1, uuid);
        result = stmt.executeQuery();
        if (result.next()) {
          String s = result.getString(1);
          System.out.println("result: " + s);
        } else {
          System.out.println("no result");
        }
        result.close();
        stmt.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
      dbc.close();
    }

    dbc = dataSource.getConnection();
    System.out.println("----------------------------------------------------");

    final String uuid = "927b8a04-c319-4139-b985-79b1cbc43871";
    stmt = dbc.prepareStatement("select uuid_col from id2user where uuid_col = ?");
    stmt.setString(1, uuid);
    result = stmt.executeQuery();
    if (result.next()) {
      String s = result.getString(1);
      System.out.println("result: " + s);
    } else {
      System.out.println("no result");
    }
    result.close();
    stmt.close();
    dbc.close();
  }
}