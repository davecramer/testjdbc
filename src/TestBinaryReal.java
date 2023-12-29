import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestBinaryReal {
    public static void main(String[] args) {
        String url="jdbc:postgresql://localhost/postgres";
        String user = "davecra";
        try (Connection connection = DriverManager.getConnection(url, user, "password")){
            connection.createStatement().execute("CREATE TABLE if not exists number("
                    + "name character varying(30) NOT NULL,"
                    + "dim1 real DEFAULT '-1' NOT NULL)"
            );
            connection.createStatement().execute("truncate number");
            connection.createStatement().execute("insert into number (name) VALUES('first')");

        }catch ( Exception ex ){
            ex.printStackTrace();
        }
        try (PreparedStatement st = DriverManager.getConnection(url, user,"password").prepareStatement("SELECT dim1 FROM number WHERE name='first'")){
            for(int i=0; i<10; i++) {
                try (ResultSet rs = st.executeQuery()){
                    rs.next();
                    if (i==5) {
                        System.out.println();
                    }
                    System.out.println("p: "+rs.getString("dim1"));
                }
            }
        }catch (Exception ex ){
            ex.printStackTrace();
        }
    }
}
