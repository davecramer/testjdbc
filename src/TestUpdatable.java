import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class TestUpdatable {
    public static void main(String[] args) {
        try ( Connection con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/test","test","test") ){
            con.setAutoCommit(false);
            String sql = "SELECT * FROM plan_data where plan_id = 1";
            ResultSet rs = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE ).executeQuery(sql);
            rs.next();
            System.out.println("Starting Value: " + rs.getDate("accounting_current_start"));
            rs.updateDate("accounting_current_start", Date.valueOf("2020-01-01"));
            rs.updateRow();
            System.out.println("After Update: " + rs.getDate("accounting_current_start"));
            con.commit();

            sql = "SELECT * FROM plan_data where plan_id = 1";
            rs = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE ).executeQuery(sql);
            rs.next();
            System.out.println("After Requery: " + rs.getDate("accounting_current_start"));
        } catch (Exception ex ) {
            ex.printStackTrace();
        }
    }
}
