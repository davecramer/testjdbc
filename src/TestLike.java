/**
 * Created by davec on 2015-02-22.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestLike {

    public static void main(String[] args) throws Exception {
        // grab a database connection
        //Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test", "test", "");
             Statement st = connection.createStatement();) {

            // clean up the table if present, and populate it
            st.execute("DROP TABLE IF EXISTS LIKE_TEST");
            st.execute("CREATE TABLE LIKE_TEST (id serial, intProperty int, doubleProperty float)");
            st.execute("INSERT INTO LIKE_TEST(intProperty, doubleProperty) VALUES(0, 0)");
            st.execute("INSERT INTO LIKE_TEST(intProperty, doubleProperty) VALUES(1, 1.1)");
            st.execute("INSERT INTO LIKE_TEST(intProperty, doubleProperty) VALUES(2, 2.2)");

            // show the results of the various expressions
            try (ResultSet rs = st
                    .executeQuery("select id, doubleProperty::text, intProperty::text, doubleProperty::text LIKE ('%' || intProperty::text) from LIKE_TEST")) {
                while (rs.next()) {
                    System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", "
                            + rs.getString(3) + ", " + rs.getString(4));
                }
            }

            // run the actual query now
            int size = 0;
            try (ResultSet rs = st
                    .executeQuery("select * from LIKE_TEST where doubleProperty::text LIKE ('%' || intProperty::text)")) {
                while (rs.next()) {
                    size++;
                }
            }

            if (size != 3) {
                throw new RuntimeException("Should have gotten a count of 3, but it was : " + size);
            }

        }
    }
}
