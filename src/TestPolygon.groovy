import org.postgresql.geometric.*

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Types

/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-08-06
 * Time: 6:51 AM
 * To change this template use File | Settings | File Templates.
 */

class TestPolygon
{

  public void testSavePolygons() throws Exception {

    final String url = "jdbc:postgresql://localhost/test";
    final Connection conn = DriverManager.getConnection(url,'test', '');

    final String s = "INSERT INTO location_polygon ( id, location_id, polygon ) VALUES ( ?, ?, ? )";
    final PreparedStatement ps = conn.prepareStatement( s );
    ps.setLong( 1, 72147725131120643l );
    ps.setNull( 2, Types.OTHER );
    final PGpolygon pg = new PGpolygon();
    pg.setValue( "((30,30),(30,30.001),(30.001,30.001),(30.001,30),(30,30))" );

    ps.setObject( 3, pg );

    final int ret = ps.executeUpdate();


  }
  public void testHashSet()
  {
    final Set<Integer> useBinaryForOids = new HashSet<Integer>();
    [1016, 1184, 1021, 1022, 2950, 1009, 1015, 17, 1083, 1082, 700, 701, 21, 1266, 1005, 20, 1007, 23, 1114, 600, 603].each {val->
      useBinaryForOids.add(val)
    }
    println "should be true ${useBinaryForOids.contains(1022)}"
    println "should be false ${useBinaryForOids.contains(282933)}"
  }
  public static void main(String []args)
  {
    new TestPolygon().testSavePolygons()
  }
}
