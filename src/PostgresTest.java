import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostgresTest {
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "test", "");
             PreparedStatement pstmt = c.prepareStatement("set application_name= ?")) {
            pstmt.setString(1, "my app");
            pstmt.execute();
            try (ResultSet rs = c.createStatement().executeQuery("show application_name")){
                if (rs.next()){
                    String appName = rs.getString(1);
                    System.out.println("Application name: " + appName);
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
