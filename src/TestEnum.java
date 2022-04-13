import java.sql.*;

/**
 * Created by davec on 2015-08-19.
 */
public class TestEnum
{
    public static void main(String[] args) throws Exception {
        // grab a database connection
        //Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/test", "test", "");


             Statement st = connection.createStatement();) {
            // clean up the table if present, and populate it
            st.executeUpdate("insert into mood_table (mood) values ('sad')");

            // show the results of the various expressions
            try (ResultSet rs = st.executeQuery("select * from mood_table")) {

                ResultSetMetaData rsmd = rs.getMetaData();
                int columnType = rsmd.getColumnType(2);
                System.out.print("Column Type is "+columnType);
                while (rs.next())
                {
                    System.out.println(rs.getString(2) + ", " + rs.getObject(2));

                }

            }


        }
    }
}
