import groovy.sql.Sql

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

/**
 * Created by davec on 2016-08-15.
 */
class TestServerPrepared {
    public static void main(String []args)
    {
        Connection con = DriverManager.getConnection('jdbc:postgresql://localhost/test','test','')

        Sql sql = new Sql(con);

        sql.execute("drop table if exists foo")
        sql.execute("create table foo ( count int,  l int8 , f float4, d float8)")
        sql.execute("insert into foo values (1,2,3,4)")



        PreparedStatement preparedStatement = con.prepareStatement('update foo set count=?,  l=?,  f = ?, d=?')
        //((org.postgresql.PGStatement)preparedStatement).setPrepareThreshold();

        preparedStatement.setLong(2, 1)
        preparedStatement.setFloat(3, 2.0)
        preparedStatement.setDouble(4, 4.0)

        for (int i=1; i<10; i++) {
            preparedStatement.setInt(1, i)
            preparedStatement.executeUpdate()
        }

        preparedStatement.close()
        preparedStatement=con.prepareStatement('select count, l, f, d from foo')
        ((org.postgresql.PGStatement)preparedStatement).setPrepareThreshold(4);

        ResultSet rs = preparedStatement.executeQuery()
        for (int i=1; i<2; i++) {
            rs = preparedStatement.executeQuery()
            while (rs.next()) {
                rs.getInt(1)
                rs.getLong(2)
                rs.getFloat(3)
                rs.getDouble(4)
            }
        }
        preparedStatement.close()
        preparedStatement=con.prepareStatement('select count, l, f, d from foo')
        ((org.postgresql.PGStatement)preparedStatement).setPrepareThreshold(5);
        rs = preparedStatement.executeQuery()
        while(rs.next()){
            rs.getInt(1)
            rs.getLong(2)
            rs.getFloat(3)
            rs.getDouble(4)
        }

    }
}
