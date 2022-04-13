import groovy.sql.Sql

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager
import java.sql.PreparedStatement

/**
 * Created by davec on 2016-09-13.
 */
class TestCache {
    public static void main(String []args) {
        DriverManager.setLogWriter(new PrintWriter(System.out))
        org.postgresql.Driver.setLogLevel(org.postgresql.Driver.DEBUG)
        Connection con = DriverManager.getConnection('jdbc:postgresql://localhost/test', 'test', '')

        Integer i=1
        Long l=2
        Float f=3
        Double d=4

        Sql sql = new Sql(con);

        sql.execute("drop table if exists foo")
        sql.execute("create table foo ( i int,  l int8 , f float4, d float8)")

        for( int j=0; j < 6;j++)
            sql.executeUpdate('insert into foo (i,l,f,d) values (1,2,3,4)')

        PreparedStatement stmt = con.prepareStatement('insert into foo  values (?,?,?,?)')

        for (int j = 0; j < 6; j++) {
            stmt.setInt(1, (int)i++);
            stmt.setLong(2, (long)l++);
            stmt.setFloat(3, (float)f++);
            stmt.setDouble(4, (double)d++);
            stmt.executeUpdate()
        }
        stmt.close()
        sql.close()
    }
}
