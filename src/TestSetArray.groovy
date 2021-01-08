
import java.sql.Statement
import java.sql.Array
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by davec on 2015-10-13.
 */
class TestSetArray
{
    public static void main(String []args)
    {
        String url = "jdbc:postgresql://localhost:5432/test"
        Connection conn = null
        double []d  = new double[4];

        Properties connectionProps = new Properties();
        connectionProps.put("user", "test");
        connectionProps.put("password", "password");
        //connectionProps.put("prepareThreshold", "-1");
        conn = DriverManager.getConnection(url, connectionProps);

        conn.createStatement().execute('create table if not EXISTS arrtest (id serial, intarray integer[])')

        conn.createStatement().execute('create table if not EXISTS floatarry(id serial, floatarray float8[])')


        d[0] = 3.5;
        d[1] = -4.5;
        d[2] = 10.0 / 3;
        d[3] = 77;

        Array arr = conn.createArrayOf("float8", d);
        Object aDouble =  arr.getArray();

        println( "Double array ${aDouble}");

        PreparedStatement preparedStatement = conn.prepareStatement('insert into floatarry (floatarray) values (?)')
        //((PgConnection)conn).setForceBinary(false);

        ResultSet rs

        try
        {
            preparedStatement.setArray(1, arr)
            preparedStatement.execute()
        }
        finally
        {
            preparedStatement.close()

        }

        try
        {
            Statement stmt = conn.createStatement();

            rs = conn.createStatement().executeQuery('select * from floatarry')

            while(rs.next())
            {
                Array doubles = rs.getArray(2)
                Object fl = doubles.getArray()
                Double [] doubles1 = (Double [])fl
                println("Floats " + doubles.toString() )
            }
        }
        finally
        {
            if (rs != null)
                rs.close()
        }

        int [] intarray = [1,2,3]


        Array sqlArray = conn.createArrayOf('int',intarray)
        if (sqlArray == null) println('Error creating array of int')

        sqlArray = conn.createArrayOf('INT',intarray)
        if (sqlArray == null) println('Error creating array of INT')

        sqlArray = conn.createArrayOf('integer',intarray)
        if (sqlArray == null) println('Error creating array of integer')

        sqlArray = conn.createArrayOf('INTEGER',intarray)
        if (sqlArray == null) println('Error creating array of INTEGER')

        preparedStatement = conn.prepareStatement('insert into arrtest (intarray) values (?)')

        try
        {
            preparedStatement.setArray(1, sqlArray)
            preparedStatement.execute()
        }
        finally
        {
            preparedStatement.close()

        }
        try
        {
            rs = conn.createStatement().executeQuery('select * from arrtest')
            while(rs.next())
            {
                Array ints = rs.getArray(2)
                int [] ints1 = ints.getArray()
                println("Ints ${ints}")
            }
        }
        finally
        {
          rs.close()
        }

        for ( int i=0; i<10; i++)
        {

            try{
                rs = conn.createStatement().executeQuery('select * from arrtest');


                rs.next();
                ResultSet ars = rs.getArray(2).getResultSet();
                ars.getMetaData().getColumnType(1); // this throws NPE

            }
            finally
            {
                rs.close();
            }

        }

    }
}
