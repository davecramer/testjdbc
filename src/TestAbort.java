import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestAbort {
    static String url = "jdbc:postgresql://localhost/test";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, "test", "");
        }catch (SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static void BatchInsertData() {
        String sql = null;
        for (int i = 0; i < 1000000; i++) {
            System.out.printf("loop is %d\n", i);
            Connection conn = getConnection();
            if (conn == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) { }
                continue;
            }
            sql = String.format("INSERT INTO jdbc_test1 VALUES (%d)", i);
            System.out.printf("execute sql is %s\n", sql);
            try {
                conn.setAutoCommit(false);
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException ex ){
                ex.printStackTrace();
                return;
            }
        }
    }
    public  static void main(String []args) {
        BatchInsertData();
    }
}
