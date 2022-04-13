import java.sql.*;

public class TestWithHold {
    public static void main(String[] args) {
        try(Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost/postgres", "davec", "")) {
            con.setAutoCommit(true);
            try (Statement stmt = con.createStatement()) {
                stmt.execute("declare foo cursor with hold for select * from pg_type");
                try ( ResultSet rs = stmt.executeQuery("fetch forward 10 from foo")){
                    if (rs.next() == false) {
                        System.out.println("Error");
                    }
                }
                stmt.execute("close foo");

            }
        } catch ( SQLException ex ){
            ex.printStackTrace();
        }


    }
}
