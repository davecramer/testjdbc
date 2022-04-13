import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestMultipleQueries {
    public static void main(String[] args) throws Exception {
        // grab a database connection
        //Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test", "test", "");
             Statement st = connection.createStatement();) {
            st.execute("create temp table xyz(i int)");
             boolean isResult = st.execute("select 1; select 2");
             if (isResult) {
                 ResultSet rs = st.getResultSet();
                 while (rs.next()) {
                     System.out.println(rs.getString(1));
                 }
                 if (st.getMoreResults()) {
                     rs = st.getResultSet();
                     while (rs.next()) {
                         System.out.println(rs.getString(1));
                     }
                 }
            }
        }
    }
}
