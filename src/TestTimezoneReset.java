import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;



public class TestTimezoneReset {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/test", "test", "test")) {
            try (ResultSet rs = conn.createStatement().executeQuery("show timezone")){
                rs.next();
                System.out.println(rs.getString(1));
            }
            conn.createStatement().execute("reset timezone");
            try (ResultSet rs = conn.createStatement().executeQuery("show timezone")){
                rs.next();
                System.out.println(rs.getString(1));
            }
        } catch (Exception ex ){
            ex.printStackTrace();
        }
    }
}
