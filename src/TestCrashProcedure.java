import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TestCrashProcedure {
    public static void main(String args[]) {

        try ( Connection c = DriverManager.getConnection("jdbc:postgresql://localhost/postgres","davec","password") ){

            try( PreparedStatement ps = c.prepareStatement("call p1(?)")) {

                for (int i = 0; i < 10; i++) {
                    ps.setObject(1, i);
                    ps.execute();
                }
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("test end");
    }
}
