/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-09-10
 * Time: 5:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestStatementIsNull {
  public static void main(java.lang.String[] args) throws Exception {
    java.sql.Connection con = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost/test", "test", "");
    java.sql.DriverManager.setLogWriter(new java.io.PrintWriter(java.lang.System.err));
    //org.postgresql.Driver.class.logLevel = org.postgresql.Driver.class.DEBUG;

    java.sql.PreparedStatement pstmt = con.prepareStatement("select ? is null");
    pstmt.setTimestamp(1, new java.sql.Timestamp(java.lang.System.currentTimeMillis()));
//    pstmt.setInt(1,10)
    pstmt.execute();
    con.close();
  }

}
