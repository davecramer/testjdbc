import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestP12 {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test?sslmode=verify-full&sslrootcert=/Users/davecra/projects/jdbc/pgjdbc.git/certdir/goodroot.crt&sslkey=/Users/davecra/projects/jdbc/pgjdbc.git/certdir/goodclient.p12&sslpassword=sslpwd", "test", "")){
            try (Statement statement = connection.createStatement()){
                try (ResultSet rs = statement.executeQuery("SELECT ssl_is_used()")){
                    if (rs.next()){
                        boolean b = rs.getBoolean(1);
                        System.out.println("SSl is: "+ b);
                    }
                }
            }
        }catch ( Exception ex ){
            ex.printStackTrace();
        }
    }
}
