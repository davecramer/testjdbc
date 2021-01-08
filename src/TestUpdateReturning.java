import java.sql.*;

/**
 * Created by davec on 2015-09-21.
 */
public class TestUpdateReturning
{
    public static void main(String[] args) throws Exception {
        // grab a database connection
        //Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/davec", "davec", "");


             Statement st = connection.createStatement();) {
            // clean up the table if present, and populate it
            ;

            // show the results of the various expressions
            try (ResultSet rs = st.executeQuery("update foo set bar='baz' returning foo.*")) {

                while (rs.next())
                {
                    System.out.println(rs.getString(2) + ", " + rs.getObject(2));

                }

            }


        }
    }
}
