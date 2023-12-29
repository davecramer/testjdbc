import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Properties;

public class TestPrimaryKeyDrop {

    public static void main(String []args) throws Exception {


        String url = "jdbc:postgresql://localhost:5432/test";

        Properties props = new Properties();
        props.setProperty("user", "test");
        props.setProperty("password", "test");
        try ( Connection conn = DriverManager.getConnection(url, props) ) {
            String query = "SELECT * from TABLE1_drop where UUID=?";
            PreparedStatement preparedStatement = conn.
            prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            preparedStatement.setString(1, "2eda160e-9226-4163-ba4a-429ed7c70aae");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    rs.updateString("other_column", "TEST1"); // <-- Exception
                    rs.updateRow();
                }
            }
        }
    }
}
