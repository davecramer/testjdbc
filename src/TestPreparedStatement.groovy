import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 12-10-31
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
class TestPreparedStatement {
  public static void mainx(String[] args)
  {
    Class.forName('org.postgresql.Driver')
    Connection con = DriverManager.getConnection('jdbc:postgresql://localhost/test','test','')
    def stmt = con.createStatement()
    stmt.isClosed()

    PreparedStatement preparedStatement = con.prepareStatement('update foo set count=?, set long=?, set float = ?, set double=?')
    preparedStatement.setInt(1,100)
    preparedStatement.setLong(2,55)
    preparedStatement.setFloat(3,10.0)
    preparedStatement.setDouble(4,20.0)
    println preparedStatement.toString()
    testBatch()
  }
  public static void main(String[] args)
  {
    testUnbalanced();
  }
  public static void testBatch()
  {
    Connection con = DriverManager.getConnection('jdbc:postgresql://localhost/test','test','')
    con.setAutoCommit(false);
    PreparedStatement pstmt = con.prepareStatement(
            "UPDATE testbatch SET col1 = col1 + ? WHERE PK = ?" );
    ((org.postgresql.PGStatement)pstmt).setPrepareThreshold(0)

    // Note that the first parameter changes for every statement in the
    // batch, whereas the second parameter remains constant.
    pstmt.setInt(1, 1);
    pstmt.setInt(2, 1);
    pstmt.addBatch();

    pstmt.setInt(1, 2);
    pstmt.addBatch();

    pstmt.setInt(1, 4);
    pstmt.addBatch();

    pstmt.executeBatch();

    //now test to see that we can still use the statement after the execute
    pstmt.setInt(1, 3);
    pstmt.addBatch();

    pstmt.executeBatch();

    con.commit();

    con.rollback();

    pstmt.close();
  }
  public static void testUnbalanced() throws Exception
  {
    Connection con = DriverManager.getConnection('jdbc:postgresql://localhost/test','test','');
    PreparedStatement ps = con
            .prepareStatement("select \"id\"\" from pouet where name1 = ? and name2 = ?");
    ps.setString(1, "Roger");
    ps.setString(2, "Saucisse");
    ps.execute();


  }
}
