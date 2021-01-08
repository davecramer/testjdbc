import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-09-10
 * Time: 5:46 AM
 * To change this template use File | Settings | File Templates.
 */
class TestStatementIsNull
{
  public static void main(String[] args)
  {
    Class.forName('org.postgresql.Driver')
    Connection con = DriverManager.getConnection('jdbc:postgresql://localhost/test','test','')
    DriverManager.setLogWriter(new PrintWriter(System.err))
    org.postgresql.Driver.logLevel = org.postgresql.Driver.DEBUG

    def pstmt = con.prepareStatement("select ? is null")
    pstmt.setTimestamp(1,new Timestamp(System.currentTimeMillis()))
//    pstmt.setInt(1,10)
    pstmt.execute()
    con.close()
  }

}
