/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-07-25
 * Time: 5:40 AM
 * To change this template use File | Settings | File Templates.
 */
//import org.postgresql.jdbc4.Jdbc4PoolingDataSource

class TestPooledDatasource
{
  public static void main(String[] args)
  {
    /*
    Jdbc4PoolingDataSource pgcpds = new Jdbc4PoolingDataSource();
    pgcpds.setDataSourceName("test Pool");
    pgcpds.setServerName('localhost');
    pgcpds.setPortNumber(5432);
    pgcpds.setDatabaseName('test');
    pgcpds.setUser('test');
    pgcpds.setPassword('');
    pgcpds.setMaxConnections(40);
    pgcpds.setLogWriter(new PrintWriter(System.out));
    def connections = []

    for (int i = 0; i < 35; i++)
    {
      connections += pgcpds.getConnection()
    }
    connections.each {con ->
      con.close()
    }
    */
  }
}
