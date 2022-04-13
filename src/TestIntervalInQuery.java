import java.sql.*;
import java.util.Calendar;

/*
SELECT session.id, session.name AS session_name, session.error_level as "error-level", session.started, session.stopped, customer.name AS customer_name, count(session.id) OVER() as full_count
        FROM session
                 LEFT JOIN customer ON session.customer_id = customer.id
        WHERE session.started >= TO_DATE($1, 'DD-MM-YYYY') AND session.started <= TO_DATE($2, 'DD-MM-YYYY')
        ORDER BY session.started DESC
        LIMIT $3
            OFFSET $4
 */
public class TestIntervalInQuery {
    public static void main(String[] args) throws SQLException  {
        String url = "jdbc:postgresql://localhost/test";

        try (Connection con = DriverManager.getConnection(url, "test", "test"))
        {
            con.createStatement().execute("drop table if EXISTS  session");
            con.createStatement().execute("drop table if EXISTS  customer");

            con.createStatement().execute( "CREATE TABLE customer ( id serial primary key ,name text )");
            con.createStatement().execute("CREATE TABLE session ( id serial primary key , customer_id int references customer(id), name text, error_level text, started date, stopped date)");

            try (PreparedStatement pstmt = con.prepareStatement( "SELECT session.id, session.name AS session_name, session.error_level as \"error-level\", session.started, session.stopped, customer.name AS customer_name, count(session.id) OVER() as full_count\n" +
                    "        FROM session" +
                    "                 LEFT JOIN customer ON session.customer_id = customer.id " +
                    "        WHERE session.started >= TO_DATE(?, 'DD-MM-YYYY') AND session.started <= TO_DATE(?, 'DD-MM-YYYY') " + " + interval '1 day' " +
                    "        ORDER BY session.started DESC  LIMIT ? OFFSET ?")){
                pstmt.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
                pstmt.setDate(2, new Date(Calendar.getInstance().getTimeInMillis()));
                pstmt.setInt(3, 100);
                pstmt.setInt(4, 0);
                try (ResultSet rs = pstmt.executeQuery() ) {
                    rs.next();
                }
            }

            con.createStatement().execute("drop table session");
            con.createStatement().execute("drop table customer");
        }
    }
}
