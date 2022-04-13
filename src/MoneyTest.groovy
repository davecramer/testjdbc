import org.postgresql.util.PGmoney

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement /**
 * Created by davec on 13-11-06.
 */
class MoneyTest
{
  public static void main(String[] args)throws Exception
  {
    Class.forName("org.postgresql.Driver")
    String url="jdbc:postgresql://localhost/test"
    String usr = "test"
    Connection con = DriverManager.getConnection(url, usr,"test")
    Statement st = con.createStatement()
    st.execute('create table if not exists moneytable (id serial, dollars money)')
    PreparedStatement pstmt = con.prepareStatement('insert into moneytable (dollars) values (?)')
    pstmt.setObject(1,new PGmoney(100.00))
    pstmt.execute()
    ResultSet rs = st.executeQuery('select * from moneytable')
    def symbol = Currency.getInstance(Locale.getDefault()).symbol
    println symbol
    while(rs.next())
    {
      println rs.getObject(2)
    }


  }
}
