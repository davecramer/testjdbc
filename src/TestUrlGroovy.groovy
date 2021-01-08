import java.sql.Connection

/**
 * Created by davec on 2014-03-28.
 */
class TestUrlGroovy
{
  public static void main(String []args)
  {
    Connection con = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost/test?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",'test','')
    con.createStatement().execute("select 1")
  }
}
