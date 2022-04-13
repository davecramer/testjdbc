import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestServiceFile {
    public static void main(String[] args) throws  Exception{
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql:///somedb?service=myexample")){
            try (Statement statement = connection.createStatement()) {
                try (ResultSet rs = statement.executeQuery("select 1")){
                    rs.next();
                }
            }
        }
    }
}
