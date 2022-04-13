/**
 * Created with IntelliJ IDEA.
 * User: davec
 * Date: 13-01-20
 * Time: 7:28 AM
 * To change this template use File | Settings | File Templates.
 */

import org.postgresql.PGProperty

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.logging.LogManager

class TestLogLevel
{
  public static void main(String []args)
  {
    String [] queryModes = ['extendedForPrepared', 'extendedCacheEverything', 'extended', 'simple']

    Properties properties = new Properties()
    properties.setProperty(PGProperty.USER.getName(),"davec")
    properties.setProperty(PGProperty.PASSWORD.getName(),"password")
//    properties.setProperty(PGProperty.LOGGER_LEVEL.getName(), "TRACE")

    DriverManager.setLogWriter(new PrintWriter(System.err))

    LogManager.getLogManager().readConfiguration(LogManager.getResourceAsStream("/logging.properties"));
    for (queryMode in queryModes) {
        properties.setProperty(PGProperty.PREFER_QUERY_MODE.getName(), queryMode)

        System.err.println("\n\n\n $queryMode query mode")
        Connection connection = DriverManager.getConnection('jdbc:postgresql://localhost/test', properties)

        Statement statement = connection.createStatement()
        statement.execute('select 1')
        statement.close()
        connection.close()
    }
  }

}
