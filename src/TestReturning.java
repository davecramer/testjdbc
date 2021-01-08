import java.sql.*;
/**
 * Created by davec on 2017-05-16.
 */
public class TestReturning {

    public static void main(String[] args) throws Exception {
      try(Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "test", "");
          Statement stmt = c.createStatement())
      {
        String sql3 = "WITH id_temp AS ( select nextval('transportation_point_id_seq') id ) insert into transportation_point values ((select id from id_temp), 'foo')";
        stmt.execute("create table if not exists transportation_point(id serial primary key, address text, returning_allowed bool)");
        try (
            PreparedStatement ps1 = c.prepareStatement("insert into transportation_point (address) values (?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps2 = c.prepareStatement("insert into transportation_point (address, returning_allowed) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps3 = c.prepareStatement(sql3, new String[]{"id"} )) {
          ps1.setNull(1, Types.VARCHAR);
          ps1.executeUpdate();
          ResultSet rs1 = ps1.getGeneratedKeys();
          if (rs1.next()) {
            System.out.println("New ID: " + rs1.getInt(1));
          } else {
            throw new RuntimeException("No Generated Keys for ps1!");
          }
          if (false) {
            ps2.setNull(1, Types.VARCHAR);
            ps2.setNull(2, Types.BOOLEAN);
            ps2.executeUpdate();
            ResultSet rs2 = ps2.getGeneratedKeys();
            if (rs2.next()) {
              System.out.println("New ID: " + rs2.getInt(1));
            } else {
              throw new RuntimeException("No Generated Keys for ps2");
            }
          }
          ps3.executeUpdate();
          ResultSet rs3 = ps3.getGeneratedKeys();
          if (rs3.next()) {
            System.out.println("New Id: " + rs3.getInt(1));
          }
          else {
            throw new RuntimeException("No Generated Keys for ps3");
          }
        }
        finally {
          stmt.execute("drop TABLE transportation_point");
          stmt.close();
        }
      }
    }
}
