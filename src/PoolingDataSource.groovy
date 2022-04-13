import org.postgresql.ds.PGConnectionPoolDataSource
import org.postgresql.ds.PGPoolingDataSource
import org.postgresql.jdbc2.optional.PoolingDataSource

/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-10-17
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */


class PoolingDataSourceTest
{
  public static void main(String []args)
  {
    String DS_NAME = "JDBC 2 SE Test DataSource"

    PoolingDataSource bds = new PoolingDataSource();
    //setupDataSource(bds);
    ((PoolingDataSource) bds).setDataSourceName(DS_NAME);
    ((PoolingDataSource) bds).setInitialConnections(2);
    ((PoolingDataSource) bds).setMaxConnections(10);

    ((PGPoolingDataSource)bds).getNumAvailable()

  }
}
