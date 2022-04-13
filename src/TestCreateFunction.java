import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by davec on 2016-11-14.
 */
public class TestCreateFunction {
  public static void main(String []args) {
    try {
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/test", "test", "");
      con.createStatement().execute("CREATE OR REPLACE FUNCTION update_on_change() RETURNS TRIGGER AS $$\n" +
          "BEGIN\n" +
          "NEW.updated_at = NOW();\n" +
          "RETURN NEW;\n" +
          "END;\n" +
          "$$ language 'plpgsql';\n" +
          "\n");

      con.prepareStatement("CREATE OR REPLACE FUNCTION update_on_change() RETURNS TRIGGER AS $$\n" +
          "BEGIN\n" +
          "NEW.updated_at = NOW();\n" +
          "RETURN NEW;\n" +
          "END;\n" +
          "$$ language 'plpgsql';\n" +
          "\n").execute();

    } catch (SQLException ex) {
      ex.printStackTrace();
    }


  }
}
