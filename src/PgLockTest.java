	import java.sql.*;

public class PgLockTest {


   public static void main(String[] args)throws Exception{
       Class.forName("org.postgresql.Driver");
       String url="jdbc:postgresql://localhost/test";
       String usr = "test";
       Statement st = DriverManager.getConnection(url, usr,"test").createStatement();
       String sql = "SELECT (select relname from pg_catalog.pg_class where pg_catalog.pg_class.oid = relation) as relname, * FROM pg_locks ORDER BY pid, relation;";
       ResultSet rs = st.executeQuery(sql);
       int cols = rs.getMetaData().getColumnCount();

       for(int colnum = 1; colnum <= cols ; colnum++)
           System.out.print(rs.getMetaData().getColumnLabel(colnum) + "\t");

       System.out.println();
       System.out.println("-------------------------");

       while(rs.next()){
           for(int colnum = 1; colnum <= cols ; colnum++)
               System.out.print( rs.getObject(colnum) + "\t");
           System.out.println();
       }

   }
}
