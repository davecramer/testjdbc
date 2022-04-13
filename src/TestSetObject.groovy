/**
 * Created by davec on 2015-12-30.
 */

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

public class TestSetObject
{
    public static void main(String []args)
    {
        Connection conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/test",'test','test')
        Object param = new Object() {public String toString() {return 'tables'}}

        PreparedStatement stmt = conn.prepareStatement('SELECT * FROM information_schema.tables WHERE table_name = ?')
            stmt.setObject(1, param, Types.VARCHAR);

            ResultSet rs = stmt.executeQuery()
            while (rs.next())
                System.out.println(rs.getString(1));
    }
}
