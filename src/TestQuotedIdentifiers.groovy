/**
 * Created by davec on 2014-04-25.
 */
import groovy.sql.Sql

import java.sql.ResultSet
import java.sql.ResultSetMetaData

class TestQuotedIdentifiers
{
  public static void main(String []args)
  {
    def sql = Sql.newInstance('jdbc:postgresql:test','test','','org.postgresql.Driver')
    ResultSet dbMetaData = sql.getConnection().metaData.getIndexInfo(null, null,'sample',false,true)
    ResultSetMetaData metaData = dbMetaData.metaData
    int colCount = metaData.columnCount
    def columnNames = []
    for ( int i=1; i<= colCount;i++)
    {
      columnNames += metaData.getColumnName(i)
    }

    while (dbMetaData.next())
    {

      columnNames.each { columnName ->
        println "$columnName = ${dbMetaData.getObject(columnName)}"
      }
    }

  }
}
